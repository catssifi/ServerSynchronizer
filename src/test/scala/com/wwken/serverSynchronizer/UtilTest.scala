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


import java.nio.file.NoSuchFileException
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import com.wwken.serverSynchronizer.Util._
import com.wwken.serverSynchronizer.TestUtil._

class UtilTest extends BaseWordSpec with Matchers with BeforeAndAfter {

  val TEST_RESOURCES_BASE = "/tmp/serverSynchronizer/utilTest"
  val EMPTY_DIRECTORY_PATH = "emptyDirectory"
  
  "DirectoryToLoad" when {
    "created on empty directory" should {
      before {
        reset(TEST_RESOURCES_BASE, EMPTY_DIRECTORY_PATH, true)
      }

      "return an empty list for empty folder" in {
        Util.filesToLoad(TEST_RESOURCES_BASE, EMPTY_DIRECTORY_PATH) shouldBe empty
      }
      after {
        reset(TEST_RESOURCES_BASE, EMPTY_DIRECTORY_PATH, false)
      }
    }

    "created on non-existent directory" should {
      "throw an exception for non-existent folder" in {
        intercept[NoSuchFileException] {
          Util.filesToLoad(TEST_RESOURCES_BASE, "/do-not-exist")
        }
      }
    }

    "created on non-empty directory" should {
      
      //before {
      //  reset(TEST_RESOURCES_BASE, "", true)
      //}
     
      "return a deep list of files and folders" in {
      
        val FILE1 = joinPath(TEST_RESOURCES_BASE, "a/testa.txt")
        val FILE2 = joinPath(TEST_RESOURCES_BASE, "a/b/testb.txt")
        val FILE3 = joinPath(TEST_RESOURCES_BASE, "a/c/testc.txt")
        
        reset(TEST_RESOURCES_BASE, "", true)
        println(Util.filesToLoad(TEST_RESOURCES_BASE))
        Util.filesToLoad(TEST_RESOURCES_BASE) should not contain allOf (FILE1,FILE2,FILE3)
        
        writeToFile(FILE1, "Content-"+FILE1)
        writeToFile(FILE2, "Content-"+FILE2)
        writeToFile(FILE3, "Content-"+FILE3)
        
        
        Util.filesToLoad(TEST_RESOURCES_BASE) should contain allOf (FILE1,FILE2,FILE3)
        reset(TEST_RESOURCES_BASE, "", false)
      }
      
      
    }
  }

}
