package com.example.slick.util

import scala.slick.driver.PostgresDriver.simple._

object DatabaseWrapper {
  val ds = new org.postgresql.ds.PGSimpleDataSource
  ds.setUser("postgres")
  ds.setPassword("postgres")
  ds.setDatabaseName("scala_test")
  val internalDb = Database.forDataSource(ds)

  def db = internalDb
  def session = internalDb.createSession()
}
