package com.example.slick.v3

import java.sql.{Connection, Driver}
import com.example.slick.util.domain.Aircrafts
import com.mchange.v2.c3p0.impl.DefaultConnectionTester

import scala.slick.driver.PostgresDriver.simple._
import com.mchange.v2.c3p0.{ConnectionTester, ComboPooledDataSource}

/**
  * Created by sajith on 2/10/16.
  */
object ConnectionPool extends App {
  def db() = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass("org.postgresql.ds.PGSimpleDataSource")
    ds.setJdbcUrl("jdbc:postgresql://localhost/scala_test")
    ds.setUser("postgres")
    ds.setPassword("postgres")
    ds.setTestConnectionOnCheckout(true)
    ds.setTestConnectionOnCheckin(true)
    //    ds.setPreferredTestQuery("")
    ds.setConnectionTesterClassName("com.example.slick.v3.x")
    ds
  }

  def ds() = {
    val ds = new org.postgresql.ds.PGSimpleDataSource
    ds.setUser("postgres")
    ds.setPassword("postgres")
    ds.setDatabaseName("scala_test")
    val internalDb = Database.forDataSource(ds)
    ds
  }

  implicit var session: Session = null
  //  implicit val session = Database.forDataSource(ds()).createSession()

  val ddd = Database.forDataSource(db())

  implicit val aircrafts = TableQuery[Aircrafts]
  while (true) {
    try {
      Thread.sleep(1000)
      ddd.withSession{
        implicit session=>{
          aircrafts.foreach(println)
        }
      }
      //  implicit val session = Database.forDataSource(ds()).createSession()
    } catch {
      case e: Throwable => {
        println(e.getMessage)
//        session.close()

      }
    }
  }
}


class x extends DefaultConnectionTester {
  override def activeCheckConnection(c: Connection): Int = {
    println("activeCheckConnection")
    super.activeCheckConnection(c)
  }

  override def activeCheckConnection(c: Connection, query: String): Int = {
    println("activeCheckConnection")
    super.activeCheckConnection(c, query)
  }

  override def statusOnException(c: Connection, t: Throwable): Int = {
    println("soe")
    super.statusOnException(c, t)
  }

  override def statusOnException(c: Connection, t: Throwable, query: String): Int = {
    println("soe")
    super.statusOnException(c, t, query)
  }
}
