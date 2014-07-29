package com.example.slick.util

import scala.slick.driver.PostgresDriver.simple._

object DatabaseWrapper {
  def session = {
    val ds = new org.postgresql.ds.PGSimpleDataSource
    ds.setUser("postgres")
    ds.setPassword("postgres")
    ds.setDatabaseName("scala_test")
    val db = Database.forDataSource(ds)
    db.createSession()
  }
}
