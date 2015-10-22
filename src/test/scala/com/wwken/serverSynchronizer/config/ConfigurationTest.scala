package com.wwken.serverSynchronizer.config

import org.scalatest.{FlatSpec, Matchers}

class ConfigurationTest extends FlatSpec with Matchers {

  it should "detect ssh destination" in {
    val configuration = Configuration(
      Connection("","",""),
      "",
      "/home/user/folder",
      5,
      List("java", "py")
    )
  }

}
