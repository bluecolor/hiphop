package io.blue.model

object Status {
  val RUNNING = "RUNNING"
  val SUCCESS = "SUCCESS"
  val ERROR   = "ERROR"
  val PAUSED  = "PAUSED"
  val BLOCKED = "BLOCKED"
  val STOPPED = "STOPPED"
  val DONE    = "DONE"
  val WAITING = "WAITING"
  val WARNING = "WARNING"
  val FINISHED= "FINISHED"

  val valid = Map(
    WAITING -> List(WAITING,RUNNING,DONE,BLOCKED),
    RUNNING -> List(ERROR, SUCCESS),
    SUCCESS -> List(WAITING,RUNNING),
    ERROR -> List(WAITING, RUNNING, DONE),
    PAUSED -> List(PAUSED, WAITING, RUNNING),
    STOPPED-> List(STOPPED,WAITING,RUNNING),
    BLOCKED -> List(BLOCKED,WAITING, DONE),
    DONE -> List(DONE,WAITING,ERROR,BLOCKED)
  )

  def isValid(from: String, to: String): Boolean = valid.get(from).get.contains(to)

}
