package ScalaBase.ScalaObject

/**
 * 类的定义 class ClassName
 *     类后面的括号，类的主构造器(private val name: String, var age: Int)
 *     val(get) | var(get|set)
 *
 *     类的辅助构造器 def this(构造参数...) 在辅助构造器中必须先调用类的主构造器
 *
 *     类的构造器访问权限 private[] (构造器)
 *
 *     类的访问权限
 *         private[this] class ClassName: 在当前包可见
 *         private[包名] class ClassName: 在当前包和子包下都可见
 *
 *
 * 对象 object 定义的对象是单例的对象，对象中的方法和属性都是静态的
 *
 *     伴生对象：
 *         objectName 和 className的名称一致，并且在同一个scala源文件中，这个objectName叫做这个class的伴生对象
 *         class叫做这个object的伴生类
 *
 *     伴生类和伴生对象可以互相访问私有的成员和方法  ：private[this]修饰的不能访问
 *
 *     apply方法:objectName.apply() 和 objectName() 是一样的效果
 *
 *
 * 特质:定义特质使用Trait关键字
 *
 *     特质相当于java中的Interface, 但是比java中的Interface更强大一些，因为在trait中可以定义有具体实现的方法
 *     或者定义没有任何实现的方法
 *
 *     类或者对象可以可以动态的混入特质，并且可以同时混入多个特质
 *
 *     如果某一个类或者对象(继承)混入了某一个特质，这个特质中有一个没有实现的方法A，还有一个带有实现方法的B，
 *     这个类或者对象必须实现这个没有实现的方法A， 可以重写方法B, 必须使用override关键字
 *
 *     混入多个特质的时候，使用with关键字
 *
 * 抽象类:定义抽象类使用abstract
 *
 *     在scala中继承抽象类的话，必须使用extends关键字
 *
 *
 * 样例类、样例对象:样例类定义使用case class ClassName(属性)
 *
 *     样例对象 case object ObjectName
 *
 *     默认实现了serializable、Product
 *     支持模式匹配
 *
 * 模式匹配
 *     类似java中的 switch case， 但是比java中的更加强大
 *
 *     val  xx = var|val match {
 *         case /字符串/类型/集合/Tuple/样例类/样例对象 if 守卫 => Any
 *     }
 *
 */

class Student {

}

object Person {
  def main(args: Array[String]): Unit = {

    /**
     * 在scala中可以动态的混入N个特质
     */
    val student = new Student with Fly with ScalaTrait
    student.hello("老羊")
    student.fly("gaoxing")
    student.small("丁丁")

  }
}