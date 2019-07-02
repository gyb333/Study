package StudySpark.SparkSQL

import org.apache.spark.sql.SparkSession

import java.util.Properties

object DataSources {

  def main(args: Array[String]): Unit = {

    JDBCDataSource()

  }

  def JDBCDataSource(): Unit = {
    val url = "jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC"
    val spark = SparkSession.builder().appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    //load这个方法会读取真正mysql的数据吗？
    val tb = spark.read.format("jdbc").options(
      Map(
        "url" -> url,
        "driver" -> "com.mysql.cj.jdbc.Driver",
        "dbtable" -> "student",
        "user" -> "root",
        "password" -> "root")).load()

    tb.printSchema()
    tb.show()

    val filtered = tb.filter(r => {
      r.getAs[Int]("age") <= 13
    })
    filtered.show()

    //lambda表达式
    val r = tb.filter($"age" <= 13)

    val w = tb.where($"age" <= 13)

    val result = r.select($"id", $"name", $"age" * 10 as "age")

    val props = new Properties()
    props.put("user", "root")
    props.put("password", "root")
    result.write.mode("ignore")
      .jdbc(url, "student1", props)

    //DataFrame保存成text时出错(只能保存一列)
    //    result.write.text("file:/C:/Users/zhongduzhi/Desktop/data/text")

    //    result.write.json("file:/C:/Users/zhongduzhi/Desktop/data/json")
    //指定以后读取json类型的数据(有表头)
    val jsons = spark.read.json("file:/C:/Users/zhongduzhi/Desktop/data/json")
    jsons.printSchema()

    //    result.write.csv("file:/C:/Users/zhongduzhi/Desktop/data/csv")
    //指定以后读取csv类型的数据
    val csv = spark.read.csv("file:/C:/Users/zhongduzhi/Desktop/data/csv")
    csv.printSchema()
    val pdf = csv.toDF("id", "name", "age")

    val hdfspath = "hdfs://ns/BigData/parquet"
    //result.write.parquet(hdfspath)
    //指定以后读取parquet类型的数据
    //val parquetLine = spark.read.parquet(hdfspath)
    //val parquetLine= spark.read.format("parquet").load(hdfspath)
    val path = "file:/C:/Users/zhongduzhi/Desktop/data/parquet"
    //    result.write.parquet(path)
    val parquetLine = spark.read.parquet(path)
    parquetLine.printSchema()

    //show是Action
    parquetLine.show()
    result.show()

    spark.close()

  }

}