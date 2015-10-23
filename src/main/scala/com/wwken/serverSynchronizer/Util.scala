package com.wwken.serverSynchronizer

import java.nio.file.{Path, Paths, Files, NoSuchFileException}
import java.io._

object Util {
  
  def isFile(p: String): Boolean = {
    val f = new File(p)
    f.isFile()
  }
  
  def isDirectory(p: String): Boolean = {
    val f = new File(p)
    f.isDirectory()
  }
  
  def getFileExtension(p: String) : String = {
    val fn = new File(p).getName
    val i = fn.lastIndexOf(".")
    if (i > -1) {
      fn.substring(i+1)
    } else {
      fn
    }
  }
  
  def joinPath(p1: String, p2: String) : String = {
    val lastC = p1 takeRight 1
     if ( p1.length() == 0 || lastC == "/") {
       p1 + p2
     } else {
       p1 + "/" + p2
     }
  }

  def filesToLoad(sourceRoot: String, path: String): List[String] = {
    filesToLoad(joinPath(sourceRoot, path))
  }
  
  def filesToLoad(sourceRoot: String): List[String] = {
    val rootDirectory = findDirectory(sourceRoot) getOrElse {
      throw new NoSuchFileException(sourceRoot)
    }
    deepListFiles(rootDirectory)
      .map(file => file.toPath.toString)
  }
  
  private def findDirectory(path: String): Option[java.io.File] = {
    val directory = new File(path)
    if(directory.exists) Some(directory) else None
  }
  
  private def deepListFiles(file: java.io.File) : List[java.io.File] = {
    file
      .listFiles
      .flatMap(file => if(file.isDirectory) file :: deepListFiles(file) else List(file))
      .toList
  }
  
}


