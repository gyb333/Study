package Study.ScalaObject

/**
 * 使用关键字abstract 定义一个抽象类
 * 可以具体实现的方法
 * 也可以有具体实现的方法
 */
abstract class AbsClass {
  def eat(food: String): String

  def swimming(style: String) = {
    println(s"$style 这么游...")
  }
}