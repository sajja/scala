package com.example.scratchpad

import scala.sys.process.Process


/**
 * Created by sajith on 10/7/14.
 */

object Test {
  def x(s:String) = s
  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      // catch Exception to catch null 's'
      case e: Exception => None
    }
  }

  def main(args: Array[String]): Unit = {
    val exitValue = Process("java", Seq("-cp", "/home/sajith/.ivy2/cache/com.pagero/recipe-deployer_2.10/jars/recipe-deployer_2.10-0.42.jar:/home/sajith/.sbt/boot/scala-2.10.4/lib/scala-library.jar:/home/sajith/.ivy2/cache/io.spray/spray-client/bundles/spray-client-1.2.2.jar:/home/sajith/.ivy2/cache/io.spray/spray-can/bundles/spray-can-1.2.2.jar:/home/sajith/.ivy2/cache/io.spray/spray-io/bundles/spray-io-1.2.2.jar:/home/sajith/.ivy2/cache/io.spray/spray-util/bundles/spray-util-1.2.2.jar:/home/sajith/.ivy2/cache/io.spray/spray-http/bundles/spray-http-1.2.2.jar:/home/sajith/.ivy2/cache/org.parboiled/parboiled-scala_2.10/bundles/parboiled-scala_2.10-1.1.6.jar:/home/sajith/.ivy2/cache/org.parboiled/parboiled-core/bundles/parboiled-core-1.1.6.jar:/home/sajith/.ivy2/cache/io.spray/spray-httpx/bundles/spray-httpx-1.2.2.jar:/home/sajith/.ivy2/cache/org.jvnet.mimepull/mimepull/jars/mimepull-1.9.4.jar:/home/sajith/.ivy2/cache/net.virtual-void/json-lenses_2.10/jars/json-lenses_2.10-0.5.4.jar:/home/sajith/.ivy2/cache/io.spray/spray-json_2.10/jars/spray-json_2.10-1.2.5.jar:/home/sajith/.ivy2/cache/com.typesafe.akka/akka-actor_2.10/jars/akka-actor_2.10-2.2.4.jar:/home/sajith/.ivy2/cache/com.typesafe/config/bundles/config-1.0.2.jar:/home/sajith/.ivy2/cache/com.typesafe.akka/akka-slf4j_2.10/bundles/akka-slf4j_2.10-2.2.4.jar:/home/sajith/.ivy2/cache/fr.janalyse/janalyse-ssh_2.10/jars/janalyse-ssh_2.10-0.9.11.jar:/home/sajith/.ivy2/cache/com.typesafe/scalalogging-slf4j_2.10/jars/scalalogging-slf4j_2.10-1.0.1.jar:/home/sajith/.ivy2/cache/org.scala-lang/scala-reflect/jars/scala-reflect-2.10.0.jar:/home/sajith/.ivy2/cache/com.jcraft/jsch/jars/jsch-0.1.50.jar:/home/sajith/.ivy2/cache/org.apache.commons/commons-compress/jars/commons-compress-1.5.jar:/home/sajith/.ivy2/cache/org.tukaani/xz/jars/xz-1.2.jar:/home/sajith/.ivy2/cache/ch.qos.logback/logback-classic/jars/logback-classic-1.1.2.jar:/home/sajith/.ivy2/cache/ch.qos.logback/logback-core/jars/logback-core-1.1.2.jar:/home/sajith/.ivy2/cache/org.slf4j/slf4j-api/jars/slf4j-api-1.7.6.jar:/home/sajith/.ivy2/cache/com.github.scopt/scopt_2.10/jars/scopt_2.10-3.2.0.jar",
      "", "com.pagero.deployer.RecipeDeployer") ++ args).run()
    println(exitValue)
  }
}
