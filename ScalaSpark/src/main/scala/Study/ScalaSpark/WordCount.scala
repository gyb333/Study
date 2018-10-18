package Study.ScalaSpark

import org.apache.spark.{SparkConf,SparkContext}
import org.apache.spark.rdd.{RDD}
 
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount {
  
  val inputPath:String=""
  val outputPath:String=""
  
   def main(args:Array[String]):Unit={
      val conf = new SparkConf().setAppName("WordCount").setMaster("local")
      val sc =new SparkContext(conf)
      val lines: RDD[String] =sc.textFile(inputPath) 
      val words:RDD[String]=lines.flatMap(_.split(" "))
      val wordAndOne :RDD[(String, Int)]=words.map((_,1))
      val reduced : RDD[(String, Int)]=wordAndOne.reduceByKey(_+_)
      val sorted: RDD[(String, Int)] =reduced.sortBy(_._2, false)
      sorted.saveAsTextFile(outputPath)
      sc.stop()
   }
}