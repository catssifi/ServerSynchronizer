package com.wwken.serverSynchronizer.file

import java.text.SimpleDateFormat
import java.util.Date

trait TimePrinter extends FileSynchronizer {

  abstract override def uploadFile(fileToLoad: String): RemoteResult = {
    val dateFormat = new SimpleDateFormat("HH:mm:ss")
    val response = super.uploadFile(fileToLoad)
    println(dateFormat.format(new Date()) + ": File: " + fileToLoad + " result: " + response)
    response
  }
}
