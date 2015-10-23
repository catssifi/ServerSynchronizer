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

package com.wwken.serverSynchronizer

import com.wwken.serverSynchronizer.config.Configuration
import org.scalatest.{ BeforeAndAfter, Matchers, WordSpec }
import com.wwken.serverSynchronizer.file.ssh.SshManager
import com.wwken.serverSynchronizer.Util._
import com.wwken.serverSynchronizer.TestUtil._
import java.io.File

class BaseWordSpec extends WordSpec with Log  {
  
  val configuration = Configuration.load("./src/test/resources/test-config-localhost.json")
  val sshManager = new SshManager(configuration)
  
  class InitTest(val conf: Configuration) {
    def createAFileAndWriteContentToIt(f: String, c: String): Unit = {
      val sfileName = f
      val sfilePath = Util.joinPath(configuration.sourceRoot, sfileName)
      debug("createAFileAndWriteContentToIt sfilePath: " + sfilePath)
      TestUtil.writeToFile(sfilePath, c)
    }
    def createADir(d: String): Unit = {
      val sfileName = d
      val sfilePath = Util.joinPath(configuration.sourceRoot, d)
      val created = TestUtil.createDir(sfilePath)
      debug("createADir sfilePath: " + sfilePath + ", created: " + created)
    }
    def exists(d: String): Boolean = {
      val sfileName = d
      val sfilePath = Util.joinPath(configuration.sourceRoot, d)
      TestUtil.dirExist(sfilePath)
    }
  }
  
  val initTest = new InitTest(configuration)
  
  def reset(baseDir: String, thisDir: String, create: Boolean): Unit = {
    val p = Util.joinPath(baseDir, thisDir)
    deleteFile(p)
    if (create) {
      new File(Util.joinPath(baseDir, thisDir)).mkdirs()
    }
  }
  
  def resetRemote(): Unit = {
    sshManager.removeRoot()
  }
  
  def generateFileName(extenstion: String = "txt") : String = {
    getRunningClassName+"-test-" + BaseWordSpec.inc + "." + extenstion
  }
  
  def generateDirName(parentDir: String = "") : String = {
    Util.joinPath(parentDir, "dir-"+getRunningClassName)
  }
  
  private def getRunningClassName(): String = {
    getClass().getSimpleName()
  } 
}

object BaseWordSpec {
    private var current = 0
    private def inc = {current += 1; current}
}