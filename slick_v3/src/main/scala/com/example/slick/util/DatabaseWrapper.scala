package com.example.slick.util

import slick.driver.PostgresDriver.api._

object DatabaseWrapper {
  val ds = {
    val ds = new org.postgresql.ds.PGSimpleDataSource
    ds.setUser("postgres")
    ds.setPortNumber(15534)
    ds.setPassword("postgres")
    ds.setDatabaseName("scala_test")
    val internalDb = Database.forDataSource(ds)
    ds
  }
  def db = Database.forDataSource(ds)
  def session = db.createSession()
}
