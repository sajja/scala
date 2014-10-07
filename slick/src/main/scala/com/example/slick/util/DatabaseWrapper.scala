package com.example.slick.util

import scala.slick.driver.PostgresDriver.simple._

object DatabaseWrapper {
  val ds = {
    val ds = new org.postgresql.ds.PGSimpleDataSource
    ds.setUser("postgres")
    ds.setPassword("postgres")
    ds.setDatabaseName("scala_test")
    val internalDb = Database.forDataSource(ds)
    ds
  }
  def db = Database.forDataSource(ds)
  def session = db.createSession()
}
