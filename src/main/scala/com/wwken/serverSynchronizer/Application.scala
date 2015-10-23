package com.wwken.serverSynchronizer

import java.io.File
import java.nio.file.Paths

import com.wwken.serverSynchronizer.config.Configuration
import com.wwken.serverSynchronizer.file.TimePrinter
import com.wwken.serverSynchronizer.file.ssh.SshManager
import com.wwken.serverSynchronizer.watch.FileWatcher

class Application(config: Configuration) extends Log {

  case class ConnectivityException(smth:String)   extends Exception(smth)
  
  val files = Util.filesToLoad(config.sourceRoot)
  val uploaderWithTimePrinter = new SshManager(config) with TimePrinter

  def run(): Unit = {
    
    testConnections()
    
    val watcher = new FileWatcher(uploaderWithTimePrinter, config.sourceRoot, config.refreshIntervalInSeconds, config.fileTypesToBeWatched)
    watcher.addToWatched(Paths.get(config.sourceRoot))
    files map {
      file => Paths.get(file)
    } foreach {
      path =>
        {
          watcher.addToWatched(path)
        }
    }
    //info("all files to be watched:" + path);
    watcher.run() //infinite loop
  }
  
  private def testConnections(): Unit = {
    val result = uploaderWithTimePrinter.ls("/")
    info("In testConnections, result: " + result)
    if (result == null) {
      throw new ConnectivityException("Exception thrown");
    }
  } 
}

