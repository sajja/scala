name := "scala-db"

description := "Scala db examples"

scalaVersion := "2.11.3"

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.googlecode.flyway" % "flyway-core" % "2.1.1",
  "c3p0" % "c3p0" % "0.9.0.4",
  "com.github.sstone" %% "amqp-client" % "1.5",
  "org.scalatest" % "scalatest" % "3.0.4" % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
