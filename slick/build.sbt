name := "scala-db"

description := "Scala db examples"

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.0.2",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.googlecode.flyway" % "flyway-core" % "2.1.1",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
