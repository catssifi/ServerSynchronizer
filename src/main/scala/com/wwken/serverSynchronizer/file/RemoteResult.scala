package com.wwken.serverSynchronizer.file

case class RemoteResult(status: String, message: Option[String] = None){
  override def toString: String = {
    "status = " + status + message.map(s => "; message = " + s).getOrElse("")
  }
}

object RemoteResult {
  val SUCESS = "sucess"
  val ERROR = "error"
}