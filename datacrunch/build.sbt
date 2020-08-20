name := "Scala data crunch test"

description := "Scala db data crunch test"

scalaVersion := "2.11.1"

val akkaVersion = "2.3.2"

val PhantomVersion = "1.18.0"

resolvers ++= Seq(
  "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo" at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging" at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

import com.github.hochgi.sbt.cassandra._

CassandraPlugin.cassandraSettings

test in Test <<= (test in Test).dependsOn(startCassandra)

cassandraVersion := "2.1.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.datastax.cassandra" % "cassandra-driver-core" %"2.2.0-rc3",
  "com.websudos" %% "phantom-dsl" % PhantomVersion,
  "com.websudos" %% "phantom-testkit" % PhantomVersion % "test, provided",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)