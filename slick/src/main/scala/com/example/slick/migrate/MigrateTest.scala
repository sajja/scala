package com.example.slick.migrate

import com.example.slick.util.DatabaseWrapper
import com.googlecode.flyway.core.Flyway

object MigrateTest {
  def main(args: Array[String]) {
    val fly = new Flyway
    fly.setDataSource(DatabaseWrapper.ds)
    fly.migrate()
  }
}
