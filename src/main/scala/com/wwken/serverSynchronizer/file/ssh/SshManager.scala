package com.wwken.serverSynchronizer.file.ssh

import com.decodified.scalassh.HostKeyVerifiers.DontVerify
import com.decodified.scalassh.SSH.Result
import com.decodified.scalassh._
import com.wwken.serverSynchronizer.Log
import com.wwken.serverSynchronizer.config.Configuration
import com.wwken.serverSynchronizer.file.{ FileSynchronizer, RemoteResult }
import com.wwken.serverSynchronizer.file.ssh.RichSshClient._
import com.wwken.serverSynchronizer.Util

/**
 * @author Ken.Wu
 * @since 2015-10-20
 */

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
    _uploadFile(source, destination)
  }

  override def createDir(dirToCreate: String): RemoteResult = {
    _uploadFile(null, dirToCreate)
  }

  /*
   * if source is null, then just create the dir only
   */
  private def _uploadFile(source: String, destination: String): RemoteResult = {
    info("In _uploadFile, source: "+source+", destination: " + destination)
    connection { client =>
      client.recursivelyCreatePathIfNotExists(destination)
      if (source!=null)
        client.upload(source, destination)
    } match {
      case Left(error) => RemoteResult("error", Some(error.toString))
      case Right(_) => RemoteResult("success")
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
    connection { client =>
      client.exec(command).right.map { result =>
        r = result.stdOutAsString()
      }
    }
    RemoteResult("result", Some(r))
  }

  def exists(file: String): Boolean = {
    val lsStr = ls(file)
    lsStr.message match {
      case Some(x) => x.indexOfSlice(file) > -1
      case None => false
    }

  }
}


