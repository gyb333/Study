package Study.ScalaSpark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RDD {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDD").setMaster("local")
    val sc = new SparkContext(conf)
    //#常用Transformation(即转换，延迟加载)
    //#通过并行化scala集合创建RDD
    val rdd = sc.parallelize(Array(1, 2, 3, 4, 5, 6, 7, 8))
    //#查看该rdd的分区数量
    rdd.partitions.length

    //从集合中创建RDD，Spark主要提供了两中函数：parallelize和makeRDD
    //makeRDD的实现不可以自己指定分区的数量，而是固定为seq参数的size大小。
    val rdd1 = sc.parallelize(List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10))
    val rdd2:RDD[Int] = rdd1.map(_ * 2).sortBy(x => x, true)
    val rdd3 = rdd2.filter(_ > 10)
    val rdd4 = rdd2.map(_ * 2).sortBy(x => x + "", true)
    val rdd5 = rdd2.map(_ * 2).sortBy(x => x.toString, true)

    val rdd6 = sc.parallelize(Array("a b c", "d e f", "h i j"))
    rdd6.flatMap(_.split(" ")).collect()
    rdd6.flatMap(_.split(' ')).collect()

    val rdd7 = sc.parallelize(List(List("a b c", "a b b"), List("e f g", "a f g"), List("h i j", "a a b")))

//    List("a b c", "a b b")
//    List("a", "b", ())
     
    rdd7.flatMap(_.flatMap(_.split(" "))).collect

    //#union求并集，注意类型要一致
    val u1 = sc.parallelize(List(5, 6, 4, 7))
    val u2 = sc.parallelize(List(1, 2, 3, 4))
    val union = u1.union(u2)
    union.distinct.sortBy(x => x).collect
    union.distinct().sortBy(f=>f, true, 2)

    //#intersection求交集
    val intersection = u1.intersection(u2)

    val tup1 = sc.parallelize(List(("tom", 1), ("jerry", 2), ("kitty", 3)))
    val tup2 = sc.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7), ("tom", 2)))

    //#join(连接)
    val join = tup1.join(tup2)
    val lJoin = tup1.leftOuterJoin(tup2)
    val rJoin = tup1.rightOuterJoin(tup2)

    //#groupByKey
    val tupUnion = tup1 union tup2
    tupUnion.groupByKey
    //(tom,CompactBuffer(1, 8, 2))
    tupUnion.groupByKey.map(x => (x._1, x._2.sum))
    tupUnion.groupByKey.mapValues(_.sum).collect
    //Array((tom, CompactBuffer(1, 8, 2)), (jerry, CompactBuffer(9, 2)), (shuke, CompactBuffer(7)), (kitty, CompactBuffer(3)))

    //#WordCount
    sc.textFile("/spark/words.txt").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect
    sc.textFile("/spark/words.txt").flatMap(_.split(" ")).map((_, 1)).groupByKey.map(t => (t._1, t._2.sum)).collect
    sc.textFile("/spark/words.txt").flatMap(_.split(" ")).map((_, 1)).groupByKey.mapValues(_.sum).collect

    //#cogroup :对两个RDD中的KV元素，每个RDD中相同key中的元素分别聚合成一个集合。与reduceByKey不同的是针对两个RDD中相同的key的元素进行合并。
    val tuple1 = sc.parallelize(List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2)))
    val tuple2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
    val cg = tuple1.cogroup(tuple2)
    val cog = cg.map(t => (t._1, t._2._1.sum + t._2._2.sum))
   
 
    //#cartesian笛卡尔积
    val strArray1 = sc.parallelize(List("tom", "jerry"))
    val strArray2 = sc.parallelize(List("tom", "kitty", "shuke"))
    strArray1.cartesian(strArray2)

    //#spark action
    val rddAny = sc.parallelize(List(1, 2, 3, 4, 5), 2)

    //#collect
    rddAny.collect

    //#reduce
    val r = rddAny.reduce(_ + _)

    //#count
    rddAny.count

    //#top
    rddAny.top(2)

    //#take
    rddAny.take(2)

    //#first(similer to take(1))
    rddAny.first

    //#takeOrdered
    rddAny.takeOrdered(3)
    
    sc.stop()
    
  }

}