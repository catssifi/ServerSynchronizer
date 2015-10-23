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

package com.wwken.serverSynchronizer.file.ssh

import com.decodified.scalassh.HostKeyVerifiers.DontVerify
import com.decodified.scalassh.SSH.Result
import com.decodified.scalassh._
import com.wwken.serverSynchronizer.Log
import com.wwken.serverSynchronizer.config.Configuration
import com.wwken.serverSynchronizer.file.{ FileSynchronizer, RemoteResult }
import com.wwken.serverSynchronizer.file.ssh.RichSshClient._
import com.wwken.serverSynchronizer.Util

class SshManager(val config: Configuration) extends FileSynchronizer with Log {

  val connection = SSH(config.connection.hostname, configProvider)(_: SshClient => Result[Unit])

  private def configProvider = new HostConfigProvider {
    override def apply(v1: String): Validated[HostConfig] = {
      try {
        Right(
          HostConfig(
            login = PublicKeyLogin(config.connection.login, config.connection.keyfile),
            hostName = config.connection.hostname,
            hostKeyVerifier = DontVerify))
      } catch {
        case _: Throwable => Left(v1)
      }
    }
  }

  override def uploadFile(fileToLoad: String): RemoteResult = {
    val source = Util.joinPath(config.sourceRoot, fileToLoad)
    val destination = Util.joinPath(config.destinationRoot, fileToLoad)
    _updateRemoteFile(source, destination)
  }

  override def createDir(dirToCreate: String): RemoteResult = {
    val destination = Util.joinPath(config.destinationRoot, dirToCreate)
    _updateRemoteFile(null, destination)
  }

  /*
   * if source is null, then just create the dir only
   */
  private def _updateRemoteFile(source: String, destination: String): RemoteResult = {
    info("In _updateRemoteFile, source: "+source+", destination: " + destination)
    connection { client =>
      client.recursivelyCreatePathIfNotExists(destination, source!=null)
      if (source!=null)
        client.upload(source, destination)
    } match {
      case Left(error) => RemoteResult(RemoteResult.ERROR, Some(error.toString))
      case Right(_) => RemoteResult(RemoteResult.SUCESS)
    }
  }

  def removeFile(file: String): RemoteResult = {
    val fullPFilePath = Util.joinPath(config.destinationRoot, file)
    _exec("rm -rf " + fullPFilePath)
  }
  
  /*
   * Be careful to call this method! it is very dangerous
   */
  def removeRoot(): RemoteResult = {
    _exec("rm -rf " + config.destinationRoot)
  }

  def ls(file: String): RemoteResult = {
    val fullPFilePath = Util.joinPath(config.destinationRoot, file)
    _exec("ls -lrt " + fullPFilePath)
  }

  private def _exec(command: String): RemoteResult = {
    var r = ""
    var e = ""
    connection { client =>
      client.exec(command).right.map { result =>
        r = result.stdOutAsString()
        e = result.stdErrAsString()
      }
    }
    info("In _exec, command: "+command + ", result: "+r+", error: "+e)
    if (e.length()>0){
      RemoteResult(RemoteResult.ERROR, Some(e))
    } else {
      RemoteResult(RemoteResult.SUCESS, Some(r))
    }
  }

  def exists(file: String, isThisAFile: Boolean): Boolean = {
    val lsStr = ls(file)
    lsStr.message match {
      case Some(x) => {
        val lowerX = x.toLowerCase()
        val fileNotFound = lowerX.contains("no such file or directory")
        if(isThisAFile) {
          if (fileNotFound) {
            false
          } else {
            x.indexOfSlice(file) > -1
          }
        } else {
          val emptyResponse = lowerX.length()==0
          debug("In exists, lowerX: "+lowerX + ", emptyResponse:"+emptyResponse+", fileNotFound:"+fileNotFound)
          emptyResponse || !fileNotFound
        }
      }
      case None => false
    }
  }
}


