name := "scala-db"

description := "Scala db examples"

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  //  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",

  "com.googlecode.flyway" % "flyway-core" % "2.1.1",
  "c3p0" % "c3p0" % "0.9.0.4",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
