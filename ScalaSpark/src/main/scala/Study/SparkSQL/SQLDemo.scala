package Study.SparkSQL

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

object SQLDemo {

  def main(args: Array[String]): Unit = {

    //    base()
    join()
  }

  def base(): Unit = {
    //spark2.x SQL的编程API(SparkSession)
    //是spark2.x SQL执行的入口
    val session = SparkSession.builder()
      .appName("SQLDemo")
      .master("local[*]")
      .getOrCreate()

    //创建RDD
    val lines = session.sparkContext.textFile("hdfs://ns/person")

    //将数据进行整理
    val rowRDD = lines.map(line => {
      val fields = line.split(",")
      val id = fields(0).toLong
      val name = fields(1)
      val age = fields(2).toInt
      val fv = fields(3).toDouble
      Row(id, name, age, fv)
    })

    //结果类型，其实就是表头，用于描述DataFrame
    val schema = StructType(List(
      StructField("id", LongType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true),
      StructField("fv", DoubleType, true)))

    //创建DataFrame
    val df = session.createDataFrame(rowRDD, schema)

    import session.implicits._

    val df2: Dataset[Row] = df.where($"fv" > 98).orderBy($"fv" desc, $"age" asc)

    df2.show()

    session.stop()

  }

  def join(): Unit = {

    val spark = SparkSession.builder()
      .appName("join").master("local[*]").getOrCreate()

    var data = List("1,gyb,china", "2,tiger,usa", "3,scott,jp")
    import spark.implicits._
    val lines: Dataset[String] = spark.createDataset(data)
    val tpDs = lines.map(line => {
      val fields = line.split(",")
      val id = fields(0).toLong
      val name = fields(1)
      val code = fields(2)
      (id, name, code)
    })
    val df1 = tpDs.toDF("id", "name", "code")

    data = List("china,中国", "usa,美国")
    val nations: Dataset[String] = spark.createDataset(data)
    //对数据进行整理
    val ndataset: Dataset[(String, String)] = nations.map(l => {
      val fields = l.split(",")
      val code = fields(0)
      val cname = fields(1)
      (code, cname)
    })
    val df2 = ndataset.toDF("ename", "cname")

    println(df2.count())

    //    //第一种，创建视图
    //    df1.createTempView("v_users")
    //    df2.createTempView("v_nations")
    //    val result = spark.sql("SELECT name, cname FROM v_users u JOIN v_nations n ON u.code = n.code")

    //import org.apache.spark.sql.functions._
    val result = df1.join(df2, $"code" === $"ename", "left")
//    val result = df1.join(df2, $"code" === $"ename", "left_outer")

    result.show()

    spark.stop()

  }

}