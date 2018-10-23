package Study.SparkSQL

import org.apache.spark.sql.SparkSession
import org.apache.hadoop.conf.Configuration
import scala.collection.JavaConverters._

object HiveOnSpark {
  def main(args: Array[String]): Unit = {

    val sparkBuilder = SparkSession.builder()
      .appName("HiveOnSpark").master("local[*]")

//    val conf = new Configuration()
//    var path = System.getProperty("user.dir") + "\\Resources\\";
//    conf.addResource("core-site.xml")
//    conf.addResource("hdfs-site.xml")
//    conf.addResource(path + "hive-site.xml")
//    for (c <- conf.iterator().asScala) {
//      sparkBuilder.config(c.getKey, c.getValue)
//    }
      
      
    val spark = sparkBuilder.enableHiveSupport().getOrCreate()
    val dbName="Hive3"
    //val db =spark.sqlContext.tableNames(dbName)
    spark.catalog.setCurrentDatabase(dbName)
   
    val result = spark.sql("CREATE TABLE test (id bigint, name string)");

    result.show()

    spark.close()
  }
}