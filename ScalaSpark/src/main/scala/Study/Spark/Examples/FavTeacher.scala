package Study.Spark.Examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import java.net.URL
import org.apache.spark.sql.SparkSession

object FavTeacher {

  val master: String = "local[*]"
  val basePath: String = "hdfs://ns/BigData"
  var path = basePath + "/FavTeacher.dat"
  val resultPath = basePath + "/FavTeacher"
  val local = true

  if (local) {
    path = "file:/D:/work/Study/BigData/FavTeacher.dat"
  }

  def main(args: Array[String]): Unit = {
    RDDExec()
  }

  def RDDExec(): Unit = {
    val conf = new SparkConf().setAppName("Teacher").setMaster(master)
    val sc = new SparkContext(conf)
    val lines = sc.textFile(path)
    val result = lines.map(line => {
      val index = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      val httpHost = line.substring(0, index)
      val subject = new URL(httpHost).getHost.split("[.]")(0)
      (teacher, 1)
    }).reduceByKey(_ + _).sortBy(_._2, false)
    //if(!local)result.saveAsTextFile(resultPath)
    println(result.collect.toBuffer)
    sc.stop()

  }

  
  
  
}