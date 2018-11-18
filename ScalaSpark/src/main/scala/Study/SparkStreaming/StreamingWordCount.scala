package Study.SparkStreaming

import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingWordCount {

  def main(args: Array[String]): Unit = {

    val conf=new SparkConf().setAppName("StreamingWordCount")
      .setMaster("local[*]")
    val sc=new SparkContext(conf)

    val ssc= new StreamingContext(sc,Milliseconds(5000))
    val lines=ssc.socketTextStream("Master",8888)
    val result=lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    result.print()

    ssc.start()
    ssc.awaitTermination()
  }




=======
    wordCount()
  }

  def wordCount():Unit={
    val conf = new SparkConf().setAppName("WordCount" ).setMaster("local[2]")
    val  sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Milliseconds(5000))

    val lines = ssc.socketTextStream("Master",8888)
    val result = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    result.print()
    ssc.start()
    ssc.awaitTermination()

  }
>>>>>>> f9288a36b50714fb4f601c046a9e8c4012e52d3e
}
