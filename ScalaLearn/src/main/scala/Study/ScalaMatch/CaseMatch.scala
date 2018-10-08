

package Study.ScalaMatch
/**
 * 支持模式匹配，默认实现了Serializable接口
 * 样例类： case class 类名属性....)
 * 类名的定义必须是驼峰式，属性名称第一个字母小写
 */
case class CaseClass(sender: String, messageContent: String)

/**
 * 默认实现了Serializable接口
 * 样例对象：case object 对象名
 * 模式匹配
 * 样例对象不能疯转数据
 */
case object CheckHeartBeat

object CaseMatch {
  def main(args: Array[String]): Unit = {
    val message = new CaseClass("刘亦菲", "今天晚上吃饭")

  }
}