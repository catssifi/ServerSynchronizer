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

import com.decodified.scalassh.SshClient
import com.decodified.scalassh.Validated
import com.decodified.scalassh.CommandResult
import scala.language.implicitConversions
import com.wwken.serverSynchronizer.Log


class RichSshClient(val client:SshClient) extends Log {
  
  /*
   * path: the path to be create
   * createOnParentPath: true means create on the parent path otherwise itself
   */
  def recursivelyCreatePathIfNotExists(path: String, createOnParentPath: Boolean = false) = {
    val command = "mkdir -p "+ (if (createOnParentPath==true) getParentPath(path) else path )
    debug("In recursivelyCreatePathIfNotExists, command: " + command)
    client.exec(command)
  }
  def exec(command: String): Validated[CommandResult] = 
    client.exec(command)
  private def getParentPath(path: String) = 
    if (path.contains('/')) path.substring(0,path.lastIndexOf('/')) else path
}

object RichSshClient {
  implicit def convertToRichSshClient(client: SshClient): RichSshClient = new RichSshClient(client)
}

