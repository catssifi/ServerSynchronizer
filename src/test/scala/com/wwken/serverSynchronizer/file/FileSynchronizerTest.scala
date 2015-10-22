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

package com.wwken.serverSynchronizer.file

import org.scalatest.{ FlatSpec, Matchers }
import com.wwken.serverSynchronizer.BaseWordSpec
import com.wwken.serverSynchronizer.file.ssh.SshManager
import com.wwken.serverSynchronizer.config.Configuration
import sys.process._
import com.wwken.serverSynchronizer.Util
import com.wwken.serverSynchronizer.TestUtil
import org.scalatest.Assertions._

class FileSynchronizerTest extends BaseWordSpec with Matchers {

  "FileSynchronizerTest" when {
    "test the file uploader" should {
      val configuration = Configuration.load("./src/test/resources/test-config-localhost.json")
      //val configuration = Configuration.load("./src/test/resources/test-config-iq.json")

      val sfileName = generateFileName()
      val sshManager = new SshManager(configuration)

      //Make sure the test file is not there
      if (sshManager.exists(sfileName)) {
        //println("sfileName:"+sfileName+" exists already..deleting it now...")
        sshManager.removeFile(sfileName)
      } else {
        //println("sfileName:"+sfileName+" does not exists already")
        assert(!sshManager.exists(sfileName))
      }

      //Create a fake local file from source
      initTest.createAFileAndWriteContentToIt(sfileName, "hello This is Ken")

      //upload it to a test ssh server
      val response = sshManager.uploadFile(sfileName)

      //Now file is uploaded, make sure it exists
      assert(sshManager.exists(sfileName))
    }
  }

  

}

