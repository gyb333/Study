package Study.Spark.Examples

import java.sql.DriverManager
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object JdbcRdd {

  val getConn = () => {
    DriverManager.getConnection("jdbc:mysql://Master:3306/bigdata?characterEncoding=UTF-8", "root", "root")
  }

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("JdbcRdd").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //创建RDD，这个RDD会记录以后从MySQL中读数据

    //new 了RDD，里面没有真正要计算的数据，而是告诉这个RDD，以后触发Action时到哪里读取数据
    val jdbcRDD: RDD[(Int, String, Int)] = new JdbcRDD(
      sc,
      getConn,
      "SELECT * FROM logs WHERE id >= ? AND id < ?",
      1,
      5,
      2, //分区数量
      rs => {
        val id = rs.getInt(1)
        val name = rs.getString(2)
        val age = rs.getInt(3)
        (id, name, age)
      })

    //触发Action
    val r = jdbcRDD.collect()

    println(r.toBuffer)

    sc.stop()

  }

}