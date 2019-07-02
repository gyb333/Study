package Communication

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn



class ActorClient (host: String, port: Int) extends Actor {

  var as: ActorSelection = _
  override def preStart(): Unit = {
    val path = s"akka.tcp://Server@${host}:$port/user/gyb"
    as = context.actorSelection(path)
  }

  override def receive: Receive = {
    case "start" => println("牛魔王系列已启动...")
    case msg: String => { // shit
      as ! ClientMessage(msg) // 把客户端输入的内容发送给 服务端（actorRef）--》服务端的mailbox中 -> 服务端的receive
    }
    case ServerMessage(msg) => println(s"收到服务端消息：$msg")
  }

}

object ActorClient{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port  = 8089

    val serverHost = "127.0.0.1"
    val serverPort = 8088

    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
        """.stripMargin)

    val clientSystem = ActorSystem("client", config)

    // 创建dispatch | mailbox
    val actorRef = clientSystem.actorOf(Props(new ActorClient(serverHost, serverPort.toInt)), "NMW-002")

    actorRef ! "start" // 自己给自己发送了一条消息 到自己的mailbox => receive

    while (true) {
      val question = StdIn.readLine() // 同步阻塞的， shit
      actorRef ! question // mailbox -> receive
    }
  }
}
