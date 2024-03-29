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

import java.nio.file.{Path, Paths, Files}
import java.io._
import org.apache.commons.io.FileUtils

object TestUtil extends Log{
  
  def deleteFile(p: String) : Unit = {
    debug("now deleting directory: " + p)
    FileUtils.deleteDirectory(new File(p));
  }
  
  def dirExist(p: String) : Boolean = {
    return Files.exists(Paths.get(p))
  }
  
  // Return false if it already exists 
  // true if otherwise when trying to create
  def ensureDir(p: String) : Boolean = {
    if (dirExist(p)) {
      false
    } else {
      createDir(p)
    }
  }
  
  def createDir (p: String) : Boolean = {
    val f = new File(p)
    if (!f.exists()) {
      debug("In createDir, directory to be created: " + p)
      val res = f.mkdirs()
      debug("In createDir, directory : "+p+" created: " + res)
      res
    } else {
      debug("In createDir, directory exists! - " + p)
      false
    }
  }
  
  
  def getDir(p: String) : String = {
    val file = new java.io.File(p)
    file.getParentFile.toString
  }
  
  def writeToFile(p: String, c: String) : Unit = {
    val dp = getDir(p)
    ensureDir(dp)
    val pw = new PrintWriter(new File(p))
    pw.write(c)
    pw.close
  }

}

