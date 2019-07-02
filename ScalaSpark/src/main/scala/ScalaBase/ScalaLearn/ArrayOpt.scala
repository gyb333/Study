package ScalaBase.ScalaLearn

object ArrayOpt {
  /**
   * Array:
   *     内容都可变
   *
   *     长度可变数组（ArrayBuffer）和长度不可变数组 Array
   *
   * 在Scala中，集合分为可变集合（mutable）和不可变集合(immutable)
   *
   * 可变集合：长度可变，内容可变
   * 不可变集合：长度不可变，内容也不可变
   *val arr = Array[Int](1, 3, 5, 8, 9)
   */

  /**
   * Array ArrayBuffer
   *
   * 集合
   *     可变集合（collection.mutable）
   *         ListBuffer => 内容和长度都可以改变
   *
   *     不可变集合(collection.immutable)
   *         List => 长度和内容都不可变
   *         var list = List(1,3,4)
   *
   *
   *     Map
   *         Map[String, String]("a" -> "a", "b" -> "b")
   *         get => Option[String]
   *         getOrElse("key", defaultValue) => String
   *
   *     Set：适合存无序非重复数据，进行快速查找海量元素的等场景
   *         存储的元素是无序的，且里面的元素是没有重复的
   *        Set(1,1,2,3)
   *     Tuple：元组中可以分任意类型的数据, 最多可以放22个
   *         (1, true, "", Object)
   *         获取元组中的元素 tuple._2
   *
   *     Seq Nil：Seq是列表，适合存有序重复数据，进行快速插入/删除元素等场景
   *         Seq 中分为head tail
   *         第一个元素就是head
   *         剩余的都是tail
   *         List(9) head=9 tail=Nil
    *        var seq = Seq[String]()
    *         seq = seq :+ "hello"
   *
   *     Option Some None
   *         Some 和 None都是Option子类
   *         获取Some中的值是通过他的get方法
   *         None
   *
   *     集合相关的API操作 101
   *         aggregate()(seqOp, combOp) 对集合进行某种聚合操作
   *         count(boolean) 返回是符合条件的元素个数
   *         diff    某个集合和另外一个集合的差集
   *         distinct 对集合中的元素进行去重
   *         filter(boolean) 过滤出符合条件的元素集合
   *         flatMap  对集合进行某种映射（map）操作，然后在进行扁平化操作（flatten）
   *         flatten 扁平化操作
   *         fold()() 折叠操作
   *         foldLeft()() 从左到右折叠
   *         foldRight()()
   *         foreach(f: A => Unit) 遍历集合
   *         groupBy(key) 按照key进行分组
   *         grouped(Int) 将集合按照Int个数进行分组
   *         head 获取集合中的头元素
   *         indices 返回集合的角标范围
   *         intersect 请求两个集合的交集
   *         length 返回集合的元素个数
   *         map 对集合进行某种映射操作
   *         mkString 对集合进行格式化输出
   *         nonEmpty 判断集合是否为空
   *         reduce 聚合
   *
   *         reverse 将集合进行反转
   *         size 返回集合的长度
   *         slice(start, end) 截取集合的元素
   *         sortBy(key) 集合按照某个key进行排序
   *         sortWith(boolean) 将集合按照某种规则进行排序
   *         sorted 集合按照升序排序
   *         sum 对集合进行求和操作
   *         tail 返回集合的尾部元素列表
   *         zip 拉链操作 相同角标位置的元素组合到一起，返回一个新的集合
   */
  def main(args: Array[String]): Unit = {
    

    println("----------------------------------")
    ParallelSeq

    ArrayTest

    HelloWords()
    None
  }

  def ParallelSeq = {
    val list = List(1, 2, 3, 4, 5)
    println(list.par.sum)
    println(list.fold(10)(_ + _))
    println(list.par.fold(10)(_ + _))

    println(list.par.foldLeft(10)(_ + _))

    // list.par.aggregate()

  }

  def ArrayTest: Unit = {
    val arr = Array[Int](1, 3, 5, 8, 9)

    // map 映射
    val fx = (x: Int) => x * 10
    // arr 见过map映射操作之后会返回一个新的数组
    val r1 = arr.map(fx)
    println(r1.toBuffer)

    arr.map((x: Int) => x * 10)
    arr.map(x => x * 10)
    arr.map(_ * 10)

    // flatten 扁平化操作
    val arr1: Array[String] = Array("hello hello tom", "hello jerry")
    // Array(Array("hello","hello","tom"), Array("hello", "jerry"))
    val r2: Array[Array[String]] = arr1.map(_.split(" "))

    // Array("hello","hello","tom", "hello", "jerry") 扁平化操作
    r2.flatten.foreach(print)

    // flatMap = map -> flatten
    arr1.flatMap(_.split(" "))

    arr1.flatMap(_.split(" ")).foreach(println)

    // 求每个单词出现的数量 word count
    println("求每个单词出现的数量 word count")
    //Array("hello","hello","tom", "hello", "jerry")
    val r3 = arr1.flatMap(x => x.split(" "))
      .groupBy(x => x)
            .map(x =>(x._1,x._2.length)).toList.sortBy(x => -x._2)
//      .mapValues(x => x.length).toList.sortBy(x => -x._2)

    println(r3)
  }

  def HelloWords(): Unit = {
    val words: Array[String] = Array("hello tom hello jim", "hello hatano hello 菲菲")

    // words 数组中的每个元素进行切分

    //  Array(Array(hello,tom,hello,jim), Array(hello,hatano,hello,菲菲))
    val wordSplit: Array[Array[String]] = words.map((x: String) => x.split(" "))

    // 将数组中的Array扁平化
    // Array(hello,tom,hello,jim, hello,hatano,hello,菲菲)
    val fltWords: Array[String] = wordSplit.flatten

    // hello -> Array(hello, hello, hello, hello)
    val mapWords: Map[String, Array[String]] = fltWords.groupBy((wd: String) => wd)

    // (hello, 4), (tom, 1)。。。。
    val wrdResult: Map[String, Int] = mapWords.map(wdKV => (wdKV._1, wdKV._2.length))

    // Map不支持排序，需要将map转换成List， 调用sortBy方法按照单词数量降序排序
    val sortResult: List[(String, Int)] = wrdResult.toList.sortBy(t => -t._2)

    sortResult.foreach(t => println(t))
    println("------------------------------------------------")
    var sr= words.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.length)
    .toList.sortBy(-_._2)
    sr.foreach(println(_))
    println("------------------------------------------------")
    sr=words.flatMap(_.split(" ")).groupBy(x =>x).map(x =>(x._1,x._2.length))
    .toList.sortBy(-_._2)
    sr.foreach(println(_))
  }
}