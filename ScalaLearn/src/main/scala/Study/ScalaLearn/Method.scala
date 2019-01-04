package Study.ScalaLearn

import java.util.Date




object Method {

  def add(a: Int = 6, b: Int = 5) = {
    a + b
  }

  // add2方法拥有3个参数，第一个参数是一个函数， 第二个，第三个为Int类型的参数
  // 第一个参数：是拥有2个Int类型的参数，返回值为Int类型的函数
  def add2(f: (Int, Int) => Int, a: Int, b: Int) = {
    f(a, b) // f(1, 2) => 1 + 2
  }

  def add3(a: Int => Int, b: Int) = {
    a(b) + b // x * 10 + 6
  }

  // fxx: (Int, Int) => Int
  val fxx = (a: Int, b: Int) => a + b

  val f1 = (x: Int) => x * 10

  val f: (Int, Int) => Int = (a, b) => a + b
  (a: Int, b: Int) => a + b

  // 高阶函数: 将其他函数作为参数或其结果是函数的函数

  // 定义一个方法, 参数为带一个整型参数返回值为整型的函数f 和 一个整型参数v, 返回值为一个函数
  def apply(f: Int => String, v: Int) = f(v) // "[" + 10.toString() + "]"

  // 定义一个方法, 参数为一个整型参数, 返回值为String
  def layout(x: Int) = "[" + x.toString() + "]" // layout: Int => String

  def main(args: Array[String]): Unit = {
    // 调用
    println(apply(layout, 10))
    println(add(1, 2, 3, 4, 5))
    println(add1(100, 1, 2, 3, 4, 5))
    
    val date = new Date()
    // 调用log的时候, 传递了一个具体的时间参数, message 为待定参数
    // logBoundDate 成了一个新的函数, 只有log的部分参数(message)
    val logBoundDate = log(date, _: String)

    // 调用logBoundDate的时候, 只需要传递待传的message参数即可
    logBoundDate("fuck jerry ")

    logBoundDate("fuck 涛涛 ")
  }

  /**
   * 可变参数， 在参数类型后面加上一个通配符 *
   */
  def add(args: Int*): Int = {
    var sum = 0
    for (v <- args)sum += v
    sum
  }

  /**
   * 可变参数一般放在参数列表的末尾
   */
  def add1(initValue: Int, ints: Int*): Int = {
    var sum = initValue
    for (v <- ints) {
      sum += v
    }
    sum
  }

  /**
   * Any 任意类型
   */
  def makePerson(params: Any*): Unit = {

  }

  
   // 定义个输出的方法, 参数为date, message
    def log(date: Date, message: String) = {
        println(s"$date, $message")
    }

    
}