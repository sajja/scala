package com.example.slick.audit

import com.example.slick.audit.domain.{MyAuditedTables, MyTable, MyTables}
import slick.dbio.DBIO
import com.example.slick.util.DatabaseWrapper
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._


object MyTableComponent {
  val tables = TableQuery[MyTables]
}

object MyAuditTableComponent {
  val tables = TableQuery[MyAuditedTables]
}

object AuditTest {
  def bootstrap() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      MyTableComponent.tables.schema.drop,
      MyTableComponent.tables.schema.create,
      MyAuditTableComponent.tables.schema.drop,
      MyAuditTableComponent.tables.schema.create
    )
    Await.result(db.run(setup), 10 seconds)
  }

  def main(args: Array[String]): Unit = {
    bootstrap()
  }
}
