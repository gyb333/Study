package Study.Spark.Examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object IpLocation {

  var master: String = "local[2]"
  val basePath: String = "hdfs://ns/BigData"
  var path = basePath + "/access.dat"
  val resultPath = basePath + "/IpLocation"
  val local = false
  val rulePath = "D:\\work\\Study\\BigData\\ip.dat"

  def main(args: Array[String]): Unit = {
    if (local) {
      master = "local"
      path = "D:\\work\\Study\\BigData\\access.dat"
    }
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
    result.foreach(f => MyUtils.data2MySQL(Iterator(f)))

    //一次拿出一个分区(一个分区用一个连接，可以将一个分区中的多条数据写完在释放jdbc连接，这样更节省资源)
    result.foreachPartition(it => MyUtils.data2MySQL(it))

    println(result.collect.toBuffer)

    sc.stop()

  }

}