organization := "com.example"

version := "0.1"

scalaVersion := "2.10.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")


val akkaVersion = "2.3.6"
val sprayVersion = "1.3.2"

/* dependencies */
libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "1.4.0"
  // -- testing --
  , "org.scalatest" %% "scalatest" % "2.2.2" % "test"
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.1.4" % "test"
  // -- Logging --
  , "ch.qos.logback" % "logback-classic" % "1.1.2"
  // -- Akka --
  , "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  , "com.typesafe.akka" %% "akka-actor" % akkaVersion
  , "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  // -- Spray --
  , "io.spray" %% "spray-routing" % sprayVersion
  , "io.spray" %% "spray-can" % sprayVersion
  , "io.spray" %% "spray-httpx" % sprayVersion
  , "io.spray" %% "spray-client" % sprayVersion
  , "io.spray" %% "spray-testkit" % sprayVersion % "test",
  // -- Json --
  "io.spray" %% "spray-json" % sprayVersion,
  "org.json4s" %% "json4s-native" % "3.2.11",
  "com.typesafe.play" %% "play-json" % "2.4.0-M1"
)

Revolver.settings
