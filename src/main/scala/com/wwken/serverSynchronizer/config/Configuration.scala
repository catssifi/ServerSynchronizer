package com.wwken.serverSynchronizer.config

import spray.json.JsonParser
import com.wwken.serverSynchronizer.Util._

case class Connection(hostname:String, login: String, keyfile: String)

case class Configuration(
                          connection: Connection,
                          sourceRoot: String,
                          destinationRoot: String,
                          refreshIntervalInSeconds: Int,
                          fileTypesToBeWatched: List[String]) extends Product{
}

object Configuration{

  def load(configFileName: String = "config.json"): Configuration = {
    import com.wwken.serverSynchronizer.config.MyJsonProtocol._
    val configFile = loadConfigFile(configFileName)
    val config = JsonParser(configFile).convertTo[Configuration]
    config
  }

  private def loadConfigFile(configFileName: String): String = {
    scala.io.Source.fromFile(configFileName).getLines().mkString
  }
  
}
