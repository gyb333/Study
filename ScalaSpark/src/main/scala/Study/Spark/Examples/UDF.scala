package Study.Spark.Examples

import org.apache.spark.sql.SparkSession


/**
 * UDF
 * 		UDF 		输入一行，返回一行结果	
 * 		UDTF 		输入一行，返回多行结果	spark：flatMap
 * 		UDAF		输入多行，返回一行结果	aggregate聚合： count、sum、avg
 */

object UDF {
  
  
  def main(args:Array[String]):Unit={
    val spark =SparkSession.builder()
    .appName("UDF").master("local[*]")
    .getOrCreate()
    
    val data=spark.range(1, 100000)
    
    spark.stop()
    
    
    
  }
  
  
  
  
  
  
  
  
  
  
}