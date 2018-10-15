package Study.ScalaSpark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object RDDAPI {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDAPI").setMaster("local")
    val sc = new SparkContext(conf)
    //    mapPartitionsWithIndex
    val funcString = (index: Int, iter: Iterator[(String)]) => {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }

    //mapPartitionsWithIndex
    val funcInt = (index: Int, iter: Iterator[Int]) => {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
    rdd.mapPartitionsWithIndex(funcInt).collect

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //aggregate

    def func1(index: Int, iter: Iterator[(Int)]): Iterator[String] = {
      iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
    }
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
    rdd1.mapPartitionsWithIndex(func1).collect
    rdd1.aggregate(0)(math.max(_, _), _ + _) //0|01234|56789=0+4+9=13
    rdd1.aggregate(5)(math.max(_, _), _ + _) //5|51234|556789=5+5+9=19

    val rdd2 = sc.parallelize(List("a", "b", "c", "d", "e", "f"), 2)
    def func2(index: Int, iter: Iterator[(String)]): Iterator[String] = {
      iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
    }
    rdd2.aggregate("")(_ + _, _ + _) //|def|abc        defabc
    rdd2.aggregate("=")(_ + _, _ + _) //=|=def|=abc     ==def=abc

    val rdd3 = sc.parallelize(List("12", "23", "345", "4567"), 2)
    rdd3.aggregate("#")((x, y) => x + ":" + y, (x, y) => x + "-" + y) //#-#:345:4567-#:12:23
    rdd3.aggregate("")((x, y) => math.max(x.length, y.length).toString, (x, y) => x + y) //42

    val rdd4 = sc.parallelize(List("12", "23", "345", ""), 2)
    rdd4.aggregate("")((x, y) => x + "|" + y, (x, y) => x + "#" + y) //  #""|345|#|12|23
    rdd4.aggregate("")((x, y) => x + y + "|", (x, y) => x + "#" + y) //  #12|23|#345||
    rdd4.aggregate("")((x, y) => x.length + "|" + y.length, (x, y) => x + "#" + y) // #3|2#3|0
    rdd4.aggregate("")((x, y) => math.min(x.length, y.length).toString, (x, y) => x + y) //01

    val rdd5 = sc.parallelize(List("12", "23", "", "345"), 2)
    rdd5.aggregate("")((x, y) => math.min(x.length, y.length).toString, (x, y) => x + y) //11

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //aggregateByKey

    val pairRDD = sc.parallelize(List(("cat", 2), ("cat", 5), ("mouse", 4), ("cat", 12), ("dog", 12), ("mouse", 2)), 2)
    def funcTuple(index: Int, iter: Iterator[(String, Int)]): Iterator[String] = {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }
    pairRDD.mapPartitionsWithIndex(funcTuple).collect
    pairRDD.aggregateByKey(0)(math.max(_, _), _ + _).collect
    //Array[(String, Int)] = Array((dog,12), (cat,17), (mouse,6))
    pairRDD.aggregateByKey(100)(math.max(_, _), _ + _).collect
    //Array[(String, Int)] = Array((dog,100), (cat,200), (mouse,200))
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //checkpoint
    sc.setCheckpointDir("hdfs://ns:9000/ck")
    val rddcp = sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    rddcp.checkpoint
    rddcp.isCheckpointed
    rddcp.count
    rddcp.isCheckpointed
    rddcp.getCheckpointFile

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //coalesce, repartition
    val rddTo = sc.parallelize(1 to 10, 10)
    val rddC = rddTo.coalesce(2, false)
    rddC.partitions.length

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //collectAsMap
    val rddAM = sc.parallelize(List(("a", 1), ("b", 2)))
    rddAM.collectAsMap

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //combineByKey
    val rddKey = sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1))
    rddKey.combineByKey(x => x, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n).collect

    rddKey.combineByKey(x => x + 10, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n).collect

    val z1 = sc.parallelize(List("dog", "cat", "gnu", "salmon", "rabbit", "turkey", "wolf", "bear", "bee"), 3)
    val z2 = sc.parallelize(List(1, 1, 2, 2, 2, 1, 2, 2, 2), 3)
//    z1.zip(z2).combineByKey(List(_), (x: List[String], y: String) => x :+ y, (m: List[String], n: List[String]) => m ++ n)

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //countByKey

    val count = sc.parallelize(List(("a", 1), ("b", 2), ("b", 2), ("c", 2), ("c", 1)))
    count.countByKey
    count.countByValue

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //filterByRange

    val rddFilter = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("c", 2), ("a", 1)))
    rddFilter.filterByRange("b", "d").collect()

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //flatMapValues
    sc.parallelize(List(("a", "1 2"), ("b", "3 4")))
    .flatMapValues(_.split(" "))

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //foldByKey

    sc.parallelize(List("dog", "wolf", "cat", "bear"), 2)
    .map(x => (x.length, x)).foldByKey("")(_ + _)

    sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1))
    .foldByKey(0)(_+_)
    
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //foreachPartition
    sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    .foreachPartition(x => println(x.reduce(_ + _)))
    
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //keyBy
    sc.parallelize(List("dog", "salmon", "salmon", "rat", "elephant"), 3)
    .keyBy(_.length).collect
    
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //keys values
    val rddMap=sc.parallelize(List("dog", "tiger", "lion", "cat", "panther", "eagle"), 2)
    .map(x => (x.length, x))
    rddMap.keys.collect
    rddMap.values.collect
    
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //mapPartitions(it: Iterator => {it.map(x => x * 10)})

  }
}