name := "ServerSynchronizer"
version := "1.0"
enablePlugins(GitVersioning)
git.useGitDescribe := true

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

scalaVersion := "2.11.6"

resolvers += Resolver.sonatypeRepo("public")
resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies += "io.spray" %% "spray-json" % "1.3.1"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.4"
libraryDependencies += "org.apache.httpcomponents" % "httpmime" % "4.4"
libraryDependencies += "org.apache.httpcomponents" % "fluent-hc" % "4.4"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"
libraryDependencies += "com.decodified" %% "scala-ssh" % "0.7.0"
libraryDependencies += "com.jcraft" % "jzlib" % "1.1.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.3.0-SNAP2" % "test"

packAutoSettings
