package Study.ScalaAkka

import akka.actor.{Actor,ActorSystem,Props}

class HelloActor extends Actor {
  
  override def receive:Receive={
    case "nihao" =>println("hello world!")
    case "stop"  => {
      context.stop(self)
      context.system.terminate()
    }
  } 
}


object HelloActor{
  
    private val factory =ActorSystem("Factory")
    private val actorRef = factory.actorOf(Props[HelloActor],"HelloActor")
    
    def main(args:Array[String]):Unit={
      actorRef ! "nihao"
      actorRef ! "stop"
    }
    
  }