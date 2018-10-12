package Study.ScalaAkka

import akka.actor.AbstractActor.Receive
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props


/**
  * 高峰
  */
class FengGeActor extends Actor{

    override def receive: Receive = {
        case "start" => println("峰峰说：I'm OK !")
        case "啪" => {
            println("峰峰：那必须滴！")
            Thread.sleep(1000)
            sender() ! "啪啪"
        }
    }
}

/**
  * 马龙
  */
class LongGeActor(val fg: ActorRef) extends Actor{
    // 接受消息的
    override def receive: Receive = {
        case "start" => {
            println("龙龙：I'm OK !")
            fg ! "啪"
        }
        case "啪啪" => {
            println("你真猛!")
            Thread.sleep(1000)
            fg ! "啪"
        }
    }
}


object PingPongApp extends App {
   // actorSystem
    private val pingPongActorSystem = ActorSystem("PingPongActorSystem")

    // 通过actorSystem创建ActorRef

    // 创建FengGeActor
    private val ffActorRef = pingPongActorSystem.actorOf(Props[FengGeActor], "ff")

    // 创建LongGeActorRef
    private val mmActorRef = pingPongActorSystem.actorOf(Props(new LongGeActor(ffActorRef)), "mm")

    ffActorRef ! "start"
    mmActorRef ! "start"
}