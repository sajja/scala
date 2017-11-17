name := "Akka"

description := "Akka examples"

val akkaVersion = "2.5.3"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.reactivestreams" % "reactive-streams" % "1.0.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
