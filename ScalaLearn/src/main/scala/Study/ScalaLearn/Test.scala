package Study.ScalaLearn

object Test {
  def main(args: Array[String]): Unit = {

    /**
     * 变量的定义
     * 1. var 修饰的变量是可变的
     * 2. val 修饰的变量是不可变的， 相当于java中的final关键字修饰的变量
     * Unit 数据类型相当于java中void关键字，但是在scala它的标识形式是一对（）
     * var|val 变量的名称（：变量的类型） = 值
     */

    /**
     * 数据类型 Any ->
     *             AnyVal -> 7 数据类型：Short Int Long double float char Byte， Boolean 、Unit(void) = ()
     *             AnyRef -> String
     *
     */

    /**
     * 条件表达式 if ..else if ... else
     *
     * val i = 8
     * val r = if(i >8) i // 编译器会默认认为else部分没有返回值，即Unit = ()
     * val r1: Any = if Any else Any
     *
     */

    /**
     * 循环
     *     for(变量 <- 表达式/集合/数组; if 守卫)
     *     for(i <- 0 to 3; j <- 0 to 3 if i!=j)
     *     yield
     *     to  0 to 3 =>返回一个0到3的范围区间，左右都是闭区间，都包含边界值
     *     until 0 until 3 => 返回一个0到2的范围区间，左闭右开区间，包含左边边界值，不包含右边边界值
     */

    /**
     * 方法的定义，使用关键字def
     *
     * def 方法名称（参数列表）：方法的返回值类型 = 方法体
     *
     * 方法可以转换成函数 方法名称 _
     *
     * 函数的定义：（=>）
     *     方式一：
     *         (函数的参数列表) => 函数体
     *     val add = (x: Int, y: Int) => x + y
     *
     *     方式二：
     *         (函数的参数类型列表)=> 函数的返回值类型 = (函数的参数变量引用) => 函数体
     *     val add:(Int, Int) => Int = (a, b) => a + b
     *     val prtf: String => Unit = msg => println(msg)
     *
     * var a: Int = 2 + 2
     * def add(f:(Int, Int) => Int, a: Int, b: Int) = {
     *     f(a, b)
     * }
     *
     * 传名调用&传值调用
     *
     * val f = (a: Int, b: Int) => a + b
     * val f1 = (a: Int, b: Int) => a - b
     * val f2 = (a: Int, b: Int) => a * b
     * add(f, 2 + 8, 6) {
     *     f(10, 6) // 10 + 6
     * }
     *
     *
     */

    val name: String = "lisi"
    var age = 18

    println("name = " + name, "age = " + age)

    val sql = s"select * from xx where name = ? and province = ?"
    println(sql)

    println(f"姓名：$name%s  年龄：$age") // 该行输出有换行
    printf("%s 学费 %4.2f, 网址是%s\n", name, 1234.146516, "xx") // 该行输出没有换行

    //
    //        val stu = new Student("taotao", 18)
    println(s"${name}")
    //
    //        println(s"${stu.name}")
    //
    val i: Int = 12
    val s = if (i > 10) {
      i * i
    } else {
      100
    }
    //
    val r = if (i < 8) i // else 没有写， 编译器会自动推测出你什么都没有返回就是Unit

    val r1 = if (i < 8) i else 1

    println(r1)
     
  }

  
   
  
 
}