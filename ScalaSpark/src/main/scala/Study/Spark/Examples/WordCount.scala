package Study.Spark.Examples


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
//import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

 
object WordCount {
  var basePath: String = "hdfs://ns/bigdata"
  val local:Boolean =true
  if(local)
    basePath="file:///D:\\work\\Study\\bigdata"
  val inputPath: String = basePath + "/input/WordCount"
  val outputPath: String = basePath + "/output/WordCount"

  /**
   * shuffle 划分Task  两种task：ShuffleMapTask ResultTask
   * 一共有4个task Task类型数*分区数  2*2
   *
   */

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val lines: RDD[String] = sc.textFile(inputPath) //2个RDD
    val words: RDD[String] = lines.flatMap(_.split(" ")) //mapPartitionsRDD
    val wordAndOne: RDD[(String, Int)] = words.map((_, 1)) //mapPartitionsRDD
    val reduced: RDD[(String, Int)] = wordAndOne.reduceByKey(_ + _) //shuffleRDD
    val sorted: RDD[(String, Int)] = reduced.sortBy(_._2, false)
    println(sorted.collect().toBuffer)
    sorted.saveAsTextFile(outputPath)      //mapPartitionsRDD
    sc.stop()
  }
}