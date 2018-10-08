package Study.ScalaObject

trait StudentTrait {
  type T

  def learn(s: T) = {
    println(s)
  }
}

object Student1 extends StudentTrait {
  override type T = String

  def main(args: Array[String]): Unit = {
    Student1.learn("String")
  }
}

object Student2 extends StudentTrait {
  override type T = Int

  def main(args: Array[String]): Unit = {
    Student2.learn(100)
  }
}