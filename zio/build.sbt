name := "zio"

description := "Zio Examples"

//scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
//  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
//  "org.scalaz" %% "scalaz-core" % "7.3.0",
//  "org.apache.httpcomponents" % "httpclient" % "4.5.8",
//  "org.codehaus.groovy" % "groovy-all" % "2.5.7",
//  "org.typelevel" %% "cats-core" % "2.0.0"
)

libraryDependencies += "dev.zio" %% "zio" % "2.0.6"
libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.6"
