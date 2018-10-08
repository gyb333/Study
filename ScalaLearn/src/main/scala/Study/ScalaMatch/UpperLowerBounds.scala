package Study.ScalaMatch

class CmpComm[T: Ordering](o1: T, o2: T) {
  def bigger = {
    val cmptor = implicitly[Ordering[T]]
    if (cmptor.compare(o1, o2) > 0) o1 else o2
  }
}
class Students(val name: String, val age: Int) {
  override def toString: String = this.name + "\t" + this.age
}

object UpperLowerBounds {
  def main(args: Array[String]): Unit = {

    //        val cmpInt = new CmpLong(8L, 9L)
    //        println(cmpInt.bigger)

//    val cmpcom = new CmpComm(1, 2) // 上界的时候会报错
//    val cmpcom = new CmpComm(Integer.valueOf(1), Integer.valueOf(2))
//    val cmpcom = new CmpComm[Integer](1, 2)

    import ImpicitsObject._

    val tom = new Students("Tom", 18)
    val jim = new Students("Jim", 20)
    val cmpcom = new CmpComm(tom, jim)

    println(cmpcom.bigger)

  }
}