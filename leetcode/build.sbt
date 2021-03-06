name := "Leetcode"

description := "Leetcode problems"

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
  "org.reactivestreams" % "reactive-streams" % "1.0.0",
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.1",
  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.1",
  "io.scalac" %% "reactive-rabbit" % "1.0.3",
  "io.reactivex" %% "rxscala" % "0.25.0",
  "com.github.sstone" %% "amqp-client" % "1.5",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
