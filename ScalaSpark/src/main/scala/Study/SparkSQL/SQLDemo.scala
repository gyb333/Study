package Study.SparkSQL

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.IntegerType
 
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
 

object SQLDemo {
  
  def main(args:Array[String]):Unit={
    
    //spark2.x SQL的编程API(SparkSession)
    //是spark2.x SQL执行的入口
    val session = SparkSession.builder()
      .appName("SQLDemo")
      .master("local[*]")
      .getOrCreate()

    //创建RDD
    val lines= session.sparkContext.textFile("hdfs://ns/person")

    //将数据进行整理
    val rowRDD  = lines.map(line => {
      val fields = line.split(",")
      val id = fields(0).toLong
      val name = fields(1)
      val age = fields(2).toInt
      val fv = fields(3).toDouble
      Row(id, name, age, fv)
    })

    //结果类型，其实就是表头，用于描述DataFrame
    val schema  = StructType(List(
      StructField("id", LongType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true),
      StructField("fv", DoubleType, true)
    ))

    //创建DataFrame
    val df = session.createDataFrame(rowRDD, schema)

    import session.implicits._

    val df2: Dataset[Row] = df.where($"fv" > 98).orderBy($"fv" desc, $"age" asc)

    df2.show()

    session.stop()
    
    
  }
  
  
  
}