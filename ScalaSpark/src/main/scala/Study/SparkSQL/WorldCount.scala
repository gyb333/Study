package Study.SparkSQL

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

object WorldCount {

  def main(args: Array[String]): Unit = {
    val sparkBuilder = SparkSession.builder()
      .appName("WorldCount").master("local[*]")

    val conf = new Configuration()
    val core = new Path(".\\resources\\core-site.xml")
    val hdfs = new Path(".\\resources\\hdfs-site.xml")
    //    val hive = new Path(".\\resources\\hive-site.xml")
    conf.addResource(core)
    conf.addResource(hdfs)
    //    conf.addResource(hive)
    for (c <- conf.iterator().asScala) {
      sparkBuilder.config(c.getKey, c.getValue)
    }
    //val spark = sparkBuilder.enableHiveSupport().getOrCreate()
    val spark = sparkBuilder.getOrCreate()

    //(指定以后从哪里)读数据，是lazy

    //Dataset分布式数据集，是对RDD的进一步封装，是更加智能的RDD
    //dataset只有一列，默认这列叫value
    val lines: Dataset[String] = spark.read.textFile("hdfs://ns/BigData/WordCount.dat")
    //整理数据(切分压平)
    //导入隐式转换

    import spark.implicits._
    val words: Dataset[String] = lines.flatMap(_.split(","))

    //1.注册视图
    words.createTempView("v_wc")
    //执行SQL（Transformation，lazy）
    val result = spark.sql("SELECT value, COUNT(*) counts FROM v_wc GROUP BY value ORDER BY counts DESC")
    //执行Action
    result.show()

    //2.使用DataSet的API（DSL特定领域编程语言）
    val count = words.groupBy($"value" as "word").count().sort($"count" desc);
    count.show()

    val counts = words.groupBy($"value" as "word").agg(count("*") as "counts").orderBy($"counts" desc)
    counts.show()

    spark.stop()

  }

}