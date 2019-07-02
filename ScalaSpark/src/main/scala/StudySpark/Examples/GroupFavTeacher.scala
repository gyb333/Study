package StudySpark.Examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import java.net.URL
import org.apache.spark.Partitioner
import scala.collection.mutable
import org.apache.spark.sql.SparkSession

object GroupFavTeacher {
  val topN = 1
  var master: String = "local[2]"
  val basePath: String = "hdfs://ns/BigData"
  var path = basePath + "/FavTeacher.dat"
  val resultPath = basePath + "/GroupFavTeacher"
  val local = false
  if (local) {
    path = "file:/D:/work/Study/BigData/FavTeacher.dat"
  }
  def main(args: Array[String]): Unit = {
//    RDDExec()
    SQLExec()
  }

  def RDDExec(): Unit = {
    val conf = new SparkConf().setAppName("GroupFavTeacher").setMaster(master)
    val sc = new SparkContext(conf)
    val lines = sc.textFile(path) //2个RDD
    val map = lines.map(line => {
      val index = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      val httpHost = line.substring(0, index)
      val subject = new URL(httpHost).getHost.split("[.]")(0)
      ((subject, teacher), 1)
    })

    //    val result = map.reduceByKey(_ + _).groupBy(_._1._1)
    //      .mapValues(_.toList.sortBy(_._2).reverse.take(topN))

    //------------------根据业务逻辑分区优化拆分数据---------------------------------------------------
    val reduced = map.reduceByKey(_ + _) //shuffle
    val subjects = reduced.map(_._1._1).distinct().collect()
    val partitioner = new SubjectParitioner(subjects)

    //        val result = reduced.partitionBy(partitioner)     //shuffle
    //          .mapPartitions(it => {
    //            it.toList.sortBy(_._2).reverse.take(topN).iterator
    //          })
    //    //-----------------------------------------------------------

    val result = map.reduceByKey(partitioner, _ + _)
      .mapPartitions(it => {
        it.toList.sortBy(_._2).reverse.take(topN).iterator
      })

    //if (!local) result.saveAsTextFile(resultPath)
    println(result.collect().toBuffer)
    sc.stop()

  }

  def SQLExec(): Unit = {
    val spark = SparkSession.builder()
      .appName("GroupFavTeacherSQL").master(master).getOrCreate()

    val lines = spark.sparkContext.textFile(path) //spark.read.text(path)
    import spark.implicits._
    lines.map(line => {
      val index = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      val httpHost = line.substring(0, index)
      val subject = new URL(httpHost).getHost.split("[.]")(0)
      (subject, teacher)
    }).toDF("subject","teacher")
    .createTempView("v_sub_teacher")
    
    spark.sql("""
      SELECT subject,teacher,COUNT(*) counts
      FROM v_sub_teacher
      GROUP BY subject,teacher
      """)
      .createTempView("v_tmp")
      
     val result=spark.sql("""
       SELECT subject,teacher,counts
       ,row_number() over(partition by subject order by counts desc) subrk
       ,rank() over(order by counts desc) rk  
       ,dense_rank() over(order by counts desc) drk       
       FROM v_tmp
       """)
      result.show()
      spark.stop()
     
    
  }
}

//自定义分区器

class SubjectParitioner(sbs: Array[String]) extends Partitioner {

  //相当于主构造器（new的时候回执行一次）
  //用于存放规则的一个map
  val rules = new mutable.HashMap[String, Int]()
  var i = 0
  for (sb <- sbs) {
    //rules(sb) = i
    rules.put(sb, i)
    i += 1

  }

  //返回分区的数量（下一个RDD有多少分区）
  override def numPartitions: Int = sbs.length

  //根据传入的key计算分区标号
  //key是一个元组（String， String）
  override def getPartition(key: Any): Int = {
    //获取学科名称
    val subject = key.asInstanceOf[(String, String)]._1
    //根据规则计算分区编号
    rules(subject)
  }
}