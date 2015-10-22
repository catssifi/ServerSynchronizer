package com.wwken.serverSynchronizer.config

import scopt.OptionParser

case class ArgumentsConfiguration(configFileName: String)

class ArgumentsParser(args: Array[String]) {

  val parser: OptionParser[ArgumentsConfiguration] = new scopt.OptionParser[ArgumentsConfiguration]("FileUploader") {

    (opt[String]('c', "config") valueName "filename"
      text "Load application configuration from a file with a given name"
      action { (filename, config) => config.copy(configFileName = filename) })

    (help("help") abbr "h"
      text "Display this message")
  }

  private val defaultArgumentsConfiguration = ArgumentsConfiguration("config.json")
  private val argumentsConfiguration = parser.parse(args, defaultArgumentsConfiguration)

  if (!argumentsConfiguration.isDefined) System.exit(0)

  val configFileName = argumentsConfiguration.get.configFileName
}
