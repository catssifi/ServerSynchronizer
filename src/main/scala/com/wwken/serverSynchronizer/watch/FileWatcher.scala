/*
Copyright (c) 2015 Ken Wu
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.
#
# -----------------------------------------------------------------------------
#
# Author: Ken Wu
# Date: 2015
* 
*/

package com.wwken.serverSynchronizer.watch

import java.nio.file.StandardWatchEventKinds._
import java.nio.file._

import com.wwken.serverSynchronizer.HandlerFactory
import com.wwken.serverSynchronizer.Log
import com.wwken.serverSynchronizer.Util
import com.wwken.serverSynchronizer.file.{ FileSynchronizer, RemoteResult }
import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._

case class PathEvent(key: WatchKey, event: WatchEvent[_]) extends Log {
  def path = {
    val relativePath = event.context().asInstanceOf[Path]
    key.watchable().asInstanceOf[Path].resolve(relativePath)
  }

  def appliesToRegularFile = path.toFile.isFile || path.toFile.isDirectory

  def kind = event.kind
}

class FileWatcher(val fileUploader: FileSynchronizer, val sourceRoot: String, val refreshIntervalsInSeconds: Int,
    val fileTypesToBeWatchedRaw: List[String]) extends Log {

  val fileTypesToBeWatched = fileTypesToBeWatchedRaw.map(x => x.replaceAll("\\*.", ""))
  val watchedFiles = collection.mutable.Map[String, () => RemoteResult]()
  val handlerFactory = new HandlerFactory(fileUploader)
  val watcher: WatchService = FileSystems.getDefault.newWatchService()
  val refreshIntervalsInMillSeconds: Int = refreshIntervalsInSeconds * 1000

  def isToBeWatched(file: Path) = {
    if (file.toFile.isDirectory) {
      //So far i treat every directory as a watch
      true
    } else {
      val ext = Util.getFileExtension(file.toString)
      debug("this file: " + file + " has extension: " + ext)
      fileTypesToBeWatched.contains(ext) == true
    }
  }

  def addToWatched(path: Path): Unit = {
    val relativePath = Paths.get(sourceRoot).relativize(path).toString
    if (isToBeWatched(path)) {
      if (path.toFile.isDirectory) {
        path.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE)
        val handler = handlerFactory.getHandlerForDir(relativePath)
        watchedFiles += (path.toString -> handler)
      } else {
        val handler = handlerFactory.getHandlerForFile(relativePath)
        watchedFiles += (path.toString -> handler)
      }
    }
  }

  def run() = {
    info("Start the running - fileTypesToBeWatched: " + fileTypesToBeWatched + ", watchedFiles: " + watchedFiles)
    startWatching()

  }

  private def startWatching() = {
    while (true) {
      //info("In startWatching - watchedFiles: " + watchedFiles)
      val key = watcher.poll(refreshIntervalsInSeconds, TimeUnit.SECONDS);
      info("key???: " + key)
      if (key != null) {
        info("key!!!: " + key)
        key.pollEvents()
          .map(PathEvent(key, _))
          .filter(_.appliesToRegularFile)
          .foreach {
            event =>
              event.kind match {
                case ENTRY_DELETE =>
                  notifyHandlerAssignedTo(event.path)
                case ENTRY_MODIFY =>
                  notifyHandlerAssignedTo(event.path)
                case ENTRY_CREATE =>
                  if (isToBeWatched(event.path)) {
                    addToWatched(event.path)
                    notifyHandlerAssignedTo(event.path)
                  }
                case x =>
                  println(s"Unexpected event $x")
              }
          }
        key.reset()
      }
    }
  }

  /*
  More info: http://stackoverflow.com/questions/16777869/java-7-watchservice-ignoring-multiple-occurrences-of-the-same-event
   */
  //private def goSleepToAvoidEventDuplication() = Thread.sleep(100)

  def notifyHandlerAssignedTo(path: Path) = {
    info("In notifyHandlerAssignedTo - path: " + path)
    watchedFiles.get(path.toString) foreach {
      handler => handler()
    }
  }

}
