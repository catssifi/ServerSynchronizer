package com.wwken.serverSynchronizer

import com.wwken.serverSynchronizer.file.FileSynchronizer

class HandlerFactory(val uploader: FileSynchronizer) {

  def getHandlerForFile(file: String) = () => uploader.uploadFile(file)
  def getHandlerForDir(file: String) = () => uploader.createDir(file)

}
