package com.wwken.serverSynchronizer.file.ssh

import com.decodified.scalassh.SshClient
import com.decodified.scalassh.Validated
import com.decodified.scalassh.CommandResult
import scala.language.implicitConversions


class RichSshClient(val client:SshClient) {
  def recursivelyCreatePathIfNotExists(path: String) = client.exec("mkdir -p "+getParentPath(path))
  def exec(command: String): Validated[CommandResult] = client.exec(command)
  private def getParentPath(path: String) = if (path.contains('/')) path.substring(0,path.lastIndexOf('/')) else path
}

object RichSshClient {
  implicit def convertToRichSshClient(client: SshClient): RichSshClient = new RichSshClient(client)
}

