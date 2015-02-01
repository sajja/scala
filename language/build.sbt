name := "scala-db"

description := "Scala db examples"

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-dispatch" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
