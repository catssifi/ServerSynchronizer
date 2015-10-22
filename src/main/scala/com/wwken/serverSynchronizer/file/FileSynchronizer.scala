package com.wwken.serverSynchronizer.file

trait FileSynchronizer {
  def uploadFile(fileToLoad: String): RemoteResult
  def createDir(dir: String): RemoteResult
}
