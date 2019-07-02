package Study

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object MongoSparkRDD {

  val mongodbURL="mongodb://Master:27200,Second:27200,Slave:27200/"
  val input=mongodbURL+"mobike.bikes?readPreference=secondaryPreferred"
  val output=mongodbURL+"mobike.result"

  def main(args:Array[String]):Unit={
//    sparkRDD()
    sparkSQL()
  }

  def sparkRDD():Unit={
    val conf = new SparkConf()
      .setAppName("MongoSparkRDD").setMaster("local[*]")
      .set("spark.mongodb.input.uri",input)
      .set("spark.mongodb.output.uri",output)

    val sc = new SparkContext(conf)
    val res=MongoSpark.load(sc).collect()
    println(res.toBuffer)
    sc.stop()
  }

  def sparkSQL():Unit={

    val session = SparkSession.builder()
      .master("local[*]")
      .appName("MongoSparkRDD")
      .config("spark.mongodb.input.uri", input) // 指定mongodb输入
      .config("spark.mongodb.output.uri", output) // 指定mongodb输出
      .getOrCreate()
//    // 生成测试数据
//    val documents = spark.sparkContext.parallelize((1 to 10).map(i => Document.parse(s"{test: $i}")))
//    // 存储数据到mongodb
//    MongoSpark.save(documents)
    // 加载数据
    val df = MongoSpark.load(session)

    df.createTempView("v_bikes")
    val result=session.sql("select * from v_bikes where id <=10")
    // 打印输出
    result.show()
    session.stop()
  }

}
