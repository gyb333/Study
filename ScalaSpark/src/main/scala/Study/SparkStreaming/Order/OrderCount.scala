package Study.SparkStreaming.Order


import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Duration, StreamingContext}

object OrderCount {

  val AppName="OrderCount"
  val Master="local[*]"

  val group ="g1"
  //指定kafka的broker地址(sparkStream的Task直连到kafka的分区上，用更加底层的API消费，效率更高)
  val brokerList = "Master:9092,Second:9092,Slave:9092"

  //指定zk的地址，后期更新消费的偏移量时使用(以后可以使用Redis、MySQL来记录偏移量)
  val zkQuorum = "Master:2181,Second:2181,Slave:2181"
  val topic="orders"
  //创建一个 ZKGroupTopicDirs 对象,其实是指定往zk中写入数据的目录，用于保存偏移量
  val topicDirs = new ZKGroupTopicDirs(group, topic)
  //zookeeper 的host 和 ip，创建一个 client,用于跟新偏移量量的
  val zkClient = new ZkClient(zkQuorum) //是zookeeper的客户端，可以从zk中读取偏移量数据，并更新偏移量

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(AppName).setMaster(Master)
    val ssc = new StreamingContext(conf,Duration(5000))

    calculateData(ssc)

    ssc.start()
    ssc.awaitTermination()
  }

  def getKafkaParams():Map[String,String]={
    //准备kafka的参数
    val kafkaParams = Map(
      //"key.deserializer" -> classOf[StringDeserializer],
      //"value.deserializer" -> classOf[StringDeserializer],
      //"deserializer.encoding" -> "GB2312", //配置读取Kafka中数据的编码
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      //从头开始读取数据
      "auto.offset.reset" -> kafka.api.OffsetRequest.SmallestTimeString
    )
    kafkaParams
  }

  def getTopicPath():String={
    //获取 zookeeper 中的路径 "/g001/offsets/wordcount/"
    val zkTopicPath = s"${topicDirs.consumerOffsetDir}"
    zkTopicPath
  }



  def getKafkaStream(ssc:StreamingContext):InputDStream[(String, String)]={
    //创建 stream 时使用的 topic 名字集合，SparkStreaming可同时消费多个topic
    val topics: Set[String] = Set(topic)

    val zkTopicPath=getTopicPath()
    //查询该路径下是否字节点（默认有字节点为我们自己保存不同 partition 时生成的）
    //zkTopicPath  -> /g001/offsets/wordcount/
    val children = zkClient.countChildren(zkTopicPath)

    var kafkaStream: InputDStream[(String, String)] = null
    //如果 zookeeper 中有保存 offset，我们会利用这个 offset 作为 kafkaStream 的起始位置
    var fromOffsets: Map[TopicAndPartition, Long] = Map()
    val kafkaParams=getKafkaParams()
    //如果保存过 offset 注意：偏移量的查询是在Driver完成的
    if (children > 0) {
      for (i <- 0 until children) {
        val partitionOffset = zkClient.readData[String](s"$zkTopicPath/${i}")
        // wordcount/0
        val tp = TopicAndPartition(topic, i)
        //将不同 partition 对应的 offset 增加到 fromOffsets 中
        fromOffsets += (tp -> partitionOffset.toLong)
      }
      //Key: kafka的key   values: "hello tom hello jerry"
      //这个会将 kafka 的消息进行 transform，最终 kafak 的数据都会变成 (kafka的key, message) 这样的 tuple
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.key(), mmd.message())

      //通过KafkaUtils创建直连的DStream（fromOffsets参数的作用是:按照前面计算好了的偏移量继续消费数据）
      //[String, String, StringDecoder, StringDecoder,     (String, String)]
      //  key    value    key的解码方式   value的解码方式
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder
        , (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    } else {
      //如果未保存，根据 kafkaParam 的配置使用最新(largest)或者最旧的（smallest） offset
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder
        , StringDecoder](ssc, kafkaParams, topics)
    }
    kafkaStream
  }


  def calculateData(ssc:StreamingContext):Unit={
    val kafkaStream=getKafkaStream(ssc)
    val broadcastRef = IPUtils.broadcastIpRules(ssc,"")
    //偏移量的范围
    var offsetRanges = Array[OffsetRange]()

    //直连方式只有在KafkaDStream的RDD（KafkaRDD）中才能获取偏移量，那么就不能到调用DStream的Transformation
    //所以只能子在kafkaStream调用foreachRDD，获取RDD的偏移量，然后就是对RDD进行操作了
    //依次迭代KafkaDStream中的KafkaRDD
    //如果使用直连方式累加数据，那么就要在外部的数据库中进行累加（用KeyVlaue的内存数据库（NoSQL），Redis）
    //kafkaStream.foreachRDD里面的业务逻辑是在Driver端执行
    kafkaStream.foreachRDD { kafkaRDD =>
      //判断当前的kafkaStream中的RDD是否有数据
      if(!kafkaRDD.isEmpty()) {
        //只有KafkaRDD可以强转成HasOffsetRanges，并获取到偏移量
        offsetRanges = kafkaRDD.asInstanceOf[HasOffsetRanges].offsetRanges
        val lines: RDD[String] = kafkaRDD.map(_._2)

        //整理数据
        val fields: RDD[Array[String]] = lines.map(_.split(" "))

        //计算成交总金额
        CalculateUtils.calculateIncome(fields)

        //计算商品分类金额
        CalculateUtils.calculateItem(fields)

        //计算区域成交金额
        CalculateUtils.calculateZone(fields, broadcastRef)

        //偏移量跟新在哪一端（）
        for (o <- offsetRanges) {
          //  /g001/offsets/wordcount/0
          val zkPath = s"${getTopicPath()}/${o.partition}"
          //将该 partition 的 offset 保存到 zookeeper
          //  /g001/offsets/wordcount/0/20000
          ZkUtils.updatePersistentPath(zkClient, zkPath, o.untilOffset.toString)
        }
      }
    }

  }


}
