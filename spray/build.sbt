organization := "com.example"

version := "0.1"

val akkaV = "2.3.6"

val sprayV = "1.3.2"

libraryDependencies ++= Seq(
  "io.spray" %% "spray-can" % sprayV withSources() withJavadoc(),
  "io.spray" %% "spray-routing" % sprayV withSources() withJavadoc(),
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray" %% "spray-testkit" % sprayV % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
  "org.specs2" %% "specs2-core" % "2.3.11" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "com.pagero" % "archiveindex-xb_2.11" % "0.1"
)

Revolver.settings
