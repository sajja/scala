package com.example.cassandra

import com.datastax.driver.core.Cluster

object Test {
  def main(args: Array[String]): Unit = {
    val cluster = Cluster.builder.addContactPoint("localhost").build
    val sesson = cluster.connect("storage_fourmonths")
    val res = sesson.execute("SELECT * FROM sajith")


    println("XXXXXXXXXXX" + res.all().size())

  }
}
