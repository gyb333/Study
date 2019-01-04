package Study.ScalaObject

/**
 * object    单例的对象，里面定义的成员变量 和 方法都是静态的（static）
 *
 *     伴生对象： 当object 的名称和类的名称一致的时候，这个对象叫着这个类的伴生对象（必须在同一个文件中）
 *
 *     apply:object对象() -> object中的apply()方法
 *
 * class 类
 *
 *     类的主构造器：定义在类的名称后面的构造器
 *     类的辅助构造器：定义在类体中，def this()类的辅助构造器，在类的辅助构造器中必须先调用类的主构造器
 *     类的成员变量：
 *         var 对外提供了get 和 set方法
 *         val 对外值只提供了get方法
 *
 *     访问权限：
 *         成员变量 private[] 标识外部没法访问它get 或者 set方法
 *         方法    private[] 标识外部没法调用这个方法
 *         构造器  private[] 在外部无法访问
 *         类的    private[包名] 标识这个类在这个包名及其子包下可见（可访问）
 *                 private[this] 只能在当前包下可见子包不可见（不能访问）
 *
 *      伴生对象可以访问类中的私有方法private, 不能访问private[this]修饰的成员变量及方法
 */

trait Animal {
  def run()
}

trait Dog {

  val name: String = "dog"

  def run()

}

class AnimalImpl(val name: String) {

}

object AnimalObject {

  def main(args: Array[String]): Unit = {
    val cat = new AnimalImpl("Cat") with Dog {
      override val name = "fox"
      override def run(): Unit = {
        println("----")
      }
    }
    println(cat.name)
    cat.run()
  }
}