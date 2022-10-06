name := "scala"

description := "Scala db examples"

//scalaVersion := "2.11.1"

val akkaVersion = "2.5.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
//  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.1",
  "org.scalaz" %% "scalaz-core" % "7.3.0",
//  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.1",
//  "io.reactivex" %% "rxscala" % "0.25.0",
//  "com.github.sstone" %% "amqp-client" % "1.5",
  "org.apache.httpcomponents" % "httpclient" % "4.5.8",
  "org.codehaus.groovy" % "groovy-all" % "2.5.7",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.typelevel" %% "cats-core" % "2.0.0"
)

libraryDependencies += "dev.zio" %% "zio" % "2.0.2"
libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.2"
