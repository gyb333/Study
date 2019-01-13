package Study.ScalaSpark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object RDDAPI {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("RDDAPI").setMaster("local")
    val sc = new SparkContext(conf)
    //mapPartitionsWithIndex
    val funcString = (index: Int, iter: Iterator[(String)]) => {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }

    //mapPartitionsWithIndex
    val funcInt = (index: Int, iter: Iterator[Int]) => {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
    rdd.mapPartitionsWithIndex(funcInt).collect.foreach(println)

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //aggregate    input.aggregate(zeroValue)(seqOp, combOp)

    def func1(index: Int, iter: Iterator[(Int)]): Iterator[String] = {
      iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
    }
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
    rdd1.mapPartitionsWithIndex(func1).collect.foreach(println)

    var number=rdd1.aggregate(0)(math.max(_, _), _ + _) //0+4+5=9
    println(number)
    number=rdd1.aggregate(5)(math.max(_, _), _ + _) //5+5+9=19
    println(number)
    val rdd2 = sc.parallelize(List("a", "b", "c", "d", "e", "f"), 2)
    def func2(index: Int, iter: Iterator[(String)]): Iterator[String] = {
      iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
    }
    var str=rdd2.aggregate("")(_ + _, _ + _) //|def|abc        defabc
    println(str)
    str=rdd2.aggregate("=")(_ + _, _ + _) //=|=def|=abc     ==def=abc
    println(str)

    val rdd3 = sc.parallelize(List("12", "23", "345", "4567"), 2)
    str=rdd3.aggregate("")((x, y) => math.max(x.length, y.length).toString, (x, y) => x + y) //24 或42
    println(str)
    val rdd4 = sc.parallelize(List("12", "23", "345", ""), 2)
    str=rdd4.aggregate("")((x, y) => math.min(x.length, y.length).toString, (x, y) => x + y) //10或01
    println(str)
    val rdd5 = sc.parallelize(List("12", "23", "", "345"), 2)
    str=rdd5.aggregate("")((x, y) => math.min(x.length, y.length).toString, (x, y) => x + y) //11
    println(str)


    val input = sc.parallelize(List(1, 2, 3, 4),2)
    var result = input.aggregate((0, 0))(
      (acc, value) => (acc._1 + value, acc._2 + 1),
      (acc, accValue) => (acc._1 + accValue._1, acc._2 + accValue._2))
    println(result)
    result=input.aggregate((1,0))(
      (acc, value) => (acc._1 + value, acc._2 + 1),
      (acc, accValue) => (acc._1 + accValue._1, acc._2 + accValue._2))
    println(result)

    /**
     * result: (Int, Int) = (10, 4)
     * val avg = result._1 / result._2
     * avg: Int = 2.5
     * 程序的详细过程大概如下：
     * 首先定义一个初始值 (0, 0)，即我们期待的返回类型的初始值。
     * (acc,value) => (acc._1 + value, acc._2 + 1)， value是函数定义里面的T，这里是List里面的元素。
     * 所以acc._1 + value, acc._2 + 1的过程如下：
     * (0	,	0	)						(1	,	0)
     * 0+1, 0+1						1+1,0+1
     * 1+2, 1+1						2+2,1+1
     * 3+3, 2+1						4+3,2+1
     * 6+4, 3+1						7+4,3+1
     * 结果：初始值+分区计算值
     * (0+10,0+4)			(10+初始值*(分区数+1)=10+1*3,4+初始值*(分区数+1)=4+0*3)=(13,4)
     * 结果为 (10,4)。在实际Spark执行中是分布式计算，可能会把List分成多个分区，
     * 假如3个，p1(1,2), p2(3), p3(4)，经过计算各分区的的结果 (3,2), (3,1), (4,1)，
     * 这样，执行 (acc1,acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
     * 就是 (10+0*3,4+0*3) 即 (10,4)，然后再计算平均值。
     */




    //aggregateByKey

    val pairRDD = sc.parallelize(List(("cat", 2), ("cat", 5), ("mouse", 4), ("cat", 12), ("dog", 12), ("mouse", 2)), 2)
    def funcTuple(index: Int, iter: Iterator[(String, Int)]): Iterator[String] = {
      iter.map(x => "[partID:" + index + ", val: " + x + "]")
    }
    pairRDD.mapPartitionsWithIndex(funcTuple).collect.foreach(println)
    println("-----------------------------------")
    pairRDD.aggregateByKey(0)(math.max(_, _), _ + _).collect.foreach(println)
    //Array[(String, Int)] = Array((dog,12), (cat,17), (mouse,6))
    println("-----------------------------------")
    pairRDD.aggregateByKey(100)(math.max(_, _), _ + _).collect.foreach(println)
    //Array[(String, Int)] = Array((dog,100), (cat,200), (mouse,200))

////    //checkpoint
////    sc.setCheckpointDir("hdfs://ns:9000/ck")
////    val rddcp = sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
////    rddcp.checkpoint
////    rddcp.isCheckpointed
////    rddcp.count
////    rddcp.isCheckpointed
////    rddcp.getCheckpointFile

    //coalesce, repartition
    val rddTo = sc.parallelize(1 to 10, 10)
    println(rddTo.partitions.length)
    val rddC = rddTo.coalesce(2, false)
    println(rddC.partitions.length)

    //collectAsMap
    val rddAM = sc.parallelize(List(("a", 3), ("b", 2),("a",0), ("a", 1), ("b", 4), ("c", 5)))
    rddAM.collectAsMap.foreach(println)
//    //combineByKey  combineByKey(createCombiner, mergeValue, mergeCombiners)
////    val rddKey = sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1))
    println("-------------combineByKey------------")
    rddAM.combineByKey(x => x, (a: Int, b: Int) => a + b,
      (m: Int, n: Int) => m + n).collect.foreach(println)
    // Array[(String, Int)] = Array((b,6), (a,4), (c,5))
    rddAM.combineByKey(x => x + 10, (a: Int, b: Int) => a + b,
      (m: Int, n: Int) => m + n).collect.foreach(println)
    //Array[(String, Int)] = Array((b,16), (a,14), (c,15))
    println("-------------zip------------")
    val z1 = sc.parallelize(List("dog", "cat", "gnu", "salmon", "rabbit", "turkey", "wolf", "bear", "bee"), 3)
    val z2 = sc.parallelize(List(1, 1, 2, 2, 2, 1, 2, 2, 2), 3)
    val zmap = z2.zip(z1) // AArray[(Int, String)] =
    zmap.foreach(println)
    println("-------------zip combineByKey------------")
    //Array((1,dog), (1,cat), (2,gnu), (2,salmon), (2,rabbit), (1,turkey), (2,wolf), (2,bear), (2,bee))
    zmap.combineByKey(List(_), (x: List[String], y: String) => x :+ y,
      (m: List[String], n: List[String]) => m ++ n).foreach(println)
    // Array[(Int, List[String])] = Array((1,List(turkey, dog, cat)), (2,List(gnu, salmon, rabbit, wolf, bear, bee)))

    println("-------------countByKey------------")
    //countByKey
    val count = sc.parallelize(List(("a", 1), ("b", 2), ("b", 2), ("c", 2), ("c", 1)))
    println(count.countByKey)
    println(count.countByValue)

    //filterByRange
    println("-------------filterByRange------------")
    val rddFilter = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("c", 2), ("a", 1)))
    rddFilter.filterByRange("c", "d").collect().foreach(println)

    //flatMapValues
    println("-------------flatMapValues------------")
    sc.parallelize(List(("a", "1 2"), ("b", "3 4")))
      .flatMapValues(_.split(" ")).foreach(println)

    //foldByKey
    println("-------------foldByKey------------")
    sc.parallelize(List("dog", "wolf", "cat", "bear"), 2).map(x => (x.length, x))
      .foldByKey("|")(_ + _).foreach(println)

//    sc.textFile("hdfs://ns/wc").flatMap(_.split(" ")).map((_, 1))
//      .foldByKey(0)(_ + _)

    //foreachPartition
    val fp = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    fp.foreach(print) //excutor 执行     一条条
    fp.foreachPartition(x => println(x.reduce(_ + _))) //excutor 执行  一个分区

    //keyBy
    println("-------------keyBy------------")
    val kb = sc.parallelize(List("dog", "salmon", "salmon", "rat", "elephant"), 3)
    kb.keyBy(_.length).collect.foreach(println)

    //keys values
    val rddMap = sc.parallelize(List("dog", "tiger", "lion", "cat", "panther", "eagle"), 2).map(x => (x.length, x))
    println("-------------keys------------")
    rddMap.keys.collect.foreach(println)
    println("-------------values------------")
    rddMap.values.collect.foreach(println)

    //mapPartitions(it: Iterator => {it.map(x => x * 10)})
    sc.stop()
  }
}