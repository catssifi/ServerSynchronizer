package com.wwken.serverSynchronizer

import com.wwken.serverSynchronizer.file.FileSynchronizer

class HandlerFactory(val fileSynch: FileSynchronizer) {

  def getHandlerForFile(file: String) = () => fileSynch.uploadFile(file)
  def getHandlerForDir(file: String) = () => fileSynch.createDir(file)

}
