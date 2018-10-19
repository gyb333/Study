package Study.Spark.Examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

case class VO(age: Int, fv: Int)
object SortRules {
  implicit object OrderingVO extends Ordering[VO] {
    override def compare(x: VO, y: VO): Int = {
      if (x.fv == y.fv) {
        x.age - y.age
      } else {
        y.fv - x.fv
      }
    }
  }
}

object CustomSort {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("CustomSort").setMaster("local")
    val sc = new SparkContext(conf)

    //排序规则：首先按照颜值的降序，如果颜值相等，再按照年龄的升序
    val users = Array("laoduan 30 99", "laozhao 29 9999", "laozhang 28 98", "laoyang 28 99")

    //将Driver端的数据并行化变成RDD
    val lines = sc.parallelize(users)

    var result = lines.map(line => {
      val fields = line.split(" ")
      val name = fields(0)
      val age = fields(1).toInt
      val fv = fields(2).toInt
      (name, age, fv)
      //new User(name, age, fv)
    })
    //          .sortBy(u => new User(u._1, u._2, u._3))
    //          .sortBy(u => u)
    //          .sortBy(m => Man(m._2, m._3))
    //    import SortRules.OrderingVO
    //    result=result.sortBy(f => VO(f._2, f._3))
    implicit val rules = Ordering[(Int, Int)].on[(String, Int, Int)](t => (-t._3, t._2))
    result = result.sortBy(f => f)

    println(result.collect.toBuffer)

  }
}

class User(val name: String, val age: Int, val fv: Int) extends Ordered[User] with Serializable {
  override def compare(that: User): Int = {
    if (this.fv == that.fv) {
      this.age - that.age
    } else {
      -(this.fv - that.fv)
    }
  }
  override def toString: String = s"name: $name, age: $age, fv: $fv"
}

case class Man(age: Int, fv: Int) extends Ordered[Man] {
  override def compare(that: Man): Int = {
    if (this.fv == that.fv) {
      this.age - that.age
    } else {
      -(this.fv - that.fv)
    }
  }
}





