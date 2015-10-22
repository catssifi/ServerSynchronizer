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

import org.scalatest.{ BeforeAndAfter, Matchers, WordSpec }
import com.wwken.serverSynchronizer.Util._
import com.wwken.serverSynchronizer.TestUtil._

class ApplicationTest extends BaseWordSpec with Matchers with BeforeAndAfter with Log {

  "ApplicationTest" when {

    "Runs the stimulation in the sperate thread while having the main thread as the house keeper" should {

      before {
        reset(configuration.sourceRoot, "", true)
        resetRemote()

        //Now turn on the server synchronizer to start the server synchronization
        val appToRun = new Thread(new Runnable {
          def run() {
            val app = new Application(configuration)
            app.run()
          }
        })
        appToRun.start

        val serverSynchronizerStartUpTimeInSeconds = 2
        //Sleep double amount of time to give enough time for the application to start up properly
        Thread.sleep(serverSynchronizerStartUpTimeInSeconds * 1000)
      }

      val testRefreshIntervalInSeconds = configuration.refreshIntervalInSeconds * 10

      "This test the file creation logic" in {

        //Create a fake local file from source
        var sfileName = generateFileName()
        //Make sure this fake file does not exist remotely
        assert(!sshManager.exists(sfileName))

        info("creating sfileName:" + sfileName)
        initTest.createAFileAndWriteContentToIt(sfileName, "hello This is Ken")

        //Sleep double amount of time to give enough time for the remote synchronization
        Thread.sleep(testRefreshIntervalInSeconds * 1000)

        //Now make sure the remote file exists
        assert(sshManager.exists(sfileName))

      }
      after {
        //reset(configuration.sourceRoot, "", true)
        //resetRemote()
      }
    }

  }

}
