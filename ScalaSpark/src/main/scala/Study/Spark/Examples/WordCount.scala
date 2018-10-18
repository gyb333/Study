package Study.Spark.Examples

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.{RDD}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object WordCount {
  val basePath: String = "hdfs://ns/BigData"
  val inputPath:String=basePath+"/WordCount.dat"
  val outputPath:String=basePath+"/WordCount"
  
  /**
   * shuffle 划分Task  两种task：ShuffleMapTask ResultTask
   * 一共有4个task Task类型数*分区数  2*2
   * 
   */
  
   def main(args:Array[String]):Unit={
      val conf = new SparkConf().setAppName("WordCount").setMaster("local")
      val sc =new SparkContext(conf)
      val lines: RDD[String] =sc.textFile(inputPath)   //2个RDD
      val words:RDD[String]=lines.flatMap(_.split(" "))//mapPartitionsRDD
      val wordAndOne :RDD[(String, Int)]=words.map((_,1))//mapPartitionsRDD
      val reduced : RDD[(String, Int)]=wordAndOne.reduceByKey(_+_)  //shuffleRDD
      val sorted: RDD[(String, Int)] =reduced.sortBy(_._2, false)
      sorted.saveAsTextFile(outputPath)      //mapPartitionsRDD
      sc.stop()
   }
}