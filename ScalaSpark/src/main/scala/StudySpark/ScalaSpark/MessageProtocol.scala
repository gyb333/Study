package StudySpark.ScalaSpark

case class RegisterWorkerInfo(id:String,core:Int,ram:Int)

case class HearBeat(id:String)

case object RegisteredWorkerInfo

case object SendHeartBeat

case object CheckTimeOutWorker

case object RemoveTimeOutWorker

class WorkerInfo(val id:String,core:Int,ram:Int){
  var lastHeartBeatTime:Long=_
  
}