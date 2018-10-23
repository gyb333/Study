package Study.Spark.Examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object IpLocation {

  var master: String = "local[*]"
  val basePath: String = "hdfs://ns/BigData/"
  var path = basePath + "access.dat"
  val resultPath = basePath + "IpLocation"

  var rulePath = basePath + "ip.dat"

  def main(args: Array[String]): Unit = {
//    sparkCore()
//    sparkSQL()
    sparkSQL2()
  }

  def sparkCore(): Unit = {

    rulePath = "D:\\BigData\\ip.dat"

    val conf = new SparkConf().setAppName("IpLocation").setMaster(master)
    val sc = new SparkContext(conf)

    //    //取到HDFS中的ip规则
    //    val rulesLines = sc.textFile(args(0))
    //    //整理ip规则数据
    //    val rules = rulesLines.map(line => {
    //      val fields = line.split("[|]")
    //      val startNum = fields(2).toLong
    //      val endNum = fields(3).toLong
    //      val province = fields(6)
    //      (startNum, endNum, province)
    //    })

    val rules = MyUtils.readRules(rulePath)
    val broadcastRef = sc.broadcast(rules)

    val lines = sc.textFile(path)
    val result = lines.map((line: String) => {
      val fields = line.split("[|]")
      val ip = fields(1)
      //将ip转换成十进制
      val ipNum = MyUtils.ip2Long(ip)
      //进行二分法查找，通过Driver端的引用或取到Executor中的广播变量
      //（该函数中的代码是在Executor中别调用执行的，通过广播变量的引用，就可以拿到当前Executor中的广播的规则了）
      val rulesInExecutor: Array[(Long, Long, String)] = broadcastRef.value
      //查找
      var province = "未知"
      val index = MyUtils.binarySearch(rulesInExecutor, ipNum)
      if (index != -1) {
        province = rulesInExecutor(index)._3
      }
      (province, 1)
    }).reduceByKey(_ + _)

    //拿一条执行一条
    //result.foreach(f => MyUtils.data2MySQL(Iterator(f)))

    //一次拿出一个分区(一个分区用一个连接，可以将一个分区中的多条数据写完在释放jdbc连接，这样更节省资源)
    //result.foreachPartition(it => MyUtils.data2MySQL(it))

    println(result.collect.toBuffer)
    sc.stop()

  }

  def sparkSQL(): Unit = {
    val spark = SparkSession.builder()
      .appName("sparkSQL").master(master).getOrCreate()

    //取到HDFS中的ip规则
    val rulesLines = spark.sparkContext.textFile(rulePath)

    //整理ip规则数据
    val rules = rulesLines.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    })
    import spark.implicits._
    val rulesDF = rules.toDF("sNum", "eNum", "province")

    //创建RDD，读取访问日志
    val accessLines = spark.read.textFile(path)
    //整理数据
    val ipDF = accessLines.map(log => {
      //将log日志的每一行进行切分
      val fields = log.split("[|]")
      val ip = fields(1)
      //将ip转换成十进制
      val ipNum = MyUtils.ip2Long(ip)
      ipNum
    }).toDF("ip_num")

    rulesDF.createTempView("v_rules")
    ipDF.createTempView("v_ips")

    val r = spark.sql("""
          SELECT province, count(*) counts 
          FROM v_ips JOIN v_rules ON (ip_num >= snum AND ip_num <= enum) 
          GROUP BY province 
          ORDER BY counts DESC
          """)

    r.show()

    spark.stop()

  }

  def sparkSQL2(): Unit = {
    val spark = SparkSession.builder()
      .appName("sparkSQL").master(master).getOrCreate()

    //取到HDFS中的ip规则
    val rulesLines = spark.sparkContext.textFile(rulePath)
    import spark.implicits._
    //整理ip规则数据
    val rules = rulesLines.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    })
    val rulesInDriver = rules.collect()
    val broadcastRef = spark.sparkContext.broadcast(rulesInDriver)

    //创建RDD，读取访问日志
    val accessLines = spark.read.textFile(path)
    //整理数据
    val ipDF = accessLines.map(log => {
      //将log日志的每一行进行切分
      val fields = log.split("[|]")
      val ip = fields(1)
      //将ip转换成十进制
      val ipNum = MyUtils.ip2Long(ip)
      ipNum
    }).toDF("ip_num")

    ipDF.createTempView("v_ips")

    //定义一个自定义函数（UDF），并注册
    //该函数的功能是（输入一个IP地址对应的十进制，返回一个省份名称）
    spark.udf.register("ip2Province", (ipNum: Long) => {
      //查找ip规则（事先已经广播了，已经在Executor中了）
      //函数的逻辑是在Executor中执行的，怎样获取ip规则的对应的数据呢？
      //使用广播变量的引用，就可以获得
      val ipRulesInExecutor = broadcastRef.value
      //根据IP地址对应的十进制查找省份名称
      val index = MyUtils.binarySearch(ipRulesInExecutor, ipNum)
      var province = "未知"
      if (index != -1) {
        province = ipRulesInExecutor(index)._3
      }
      province
    })

    //执行SQL
    val result = spark.sql("""
                SELECT ip2Province(ip_num) province, COUNT(*) counts 
                FROM v_ips 
                GROUP BY province 
                ORDER BY counts DESC
                """)
    result.show()

    spark.stop()

  }
}