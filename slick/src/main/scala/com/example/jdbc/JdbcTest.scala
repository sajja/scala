package com.example.jdbc

import org.postgresql.ds.PGSimpleDataSource

/**
 * Created by sajith on 7/20/14.
 */
object JdbcTest {
  def main(args: Array[String]) {
    val ds = new PGSimpleDataSource()
    ds.setDatabaseName("scala_test")
    ds.setUser("postgres")
    ds.setPassword("postgres")
    val conn = ds.getConnection()
    println(conn)
    conn.close()
  }
}
