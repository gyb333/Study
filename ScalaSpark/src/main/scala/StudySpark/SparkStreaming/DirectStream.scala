package StudySpark.SparkStreaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}


object DirectStream {
  def main(args: Array[String]): Unit = {

    val group = "g0011"
    val topic = "myorder"
    //创建SparkConf，如果将任务提交到集群中，那么要去掉.setMaster("local[2]")
    val conf = new SparkConf().setAppName("DirectStream").setMaster("local[2]")
    //创建一个StreamingContext，其里面包含了一个SparkContext
    val streamingContext = new StreamingContext(conf, Seconds(5));

    //配置kafka的参数
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "Master:9092,Second:9092,Slave:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "earliest", // lastest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array(topic)
    //在Kafka中记录读取偏移量
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      //位置策略
      LocationStrategies.PreferConsistent,
      //订阅的策略
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    )


    //迭代DStream中的RDD，将每一个时间点对于的RDD拿出来
    stream.foreachRDD { rdd =>
      //获取该RDD对于的偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      //拿出对于的数据，foreach是一个aciton
      rdd.foreach{ line =>
        println(line.key() + " " + line.value())
      }
      //更新偏移量
      // some time later, after outputs have completed
      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
