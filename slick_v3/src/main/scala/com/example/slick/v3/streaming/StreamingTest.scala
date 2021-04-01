package com.example.slick.v3.streaming

import java.util.UUID

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain._
import org.reactivestreams.Publisher
import slick.backend.DatabasePublisher
import slick.driver.PostgresDriver.api._
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random


object StreamingTest {

  class StreamingLog(tag: Tag) extends Table[(String, String)](tag, "streaming_log") {
    def id = column[String]("id", O.PrimaryKey)

    // This is the primary key column
    def log = column[String]("log")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, log)
  }

  val streamingLogTable = TableQuery[StreamingLog]

  def bootstrapData() = {
    val db = DatabaseWrapper.db
    val inserts = for (i <- 0 to 999) yield streamingLogTable += (UUID.randomUUID().toString, Random.nextString(4))
    Await.result(db.run(DBIO.sequence(inserts)), Duration.Inf)
  }

  def createDb() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      streamingLogTable.schema.drop,
      streamingLogTable.schema.create
    )
    val setupFuture = db.run(setup)
    Await.result(setupFuture, 10 second)
  }


  def printAll() = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val db = DatabaseWrapper.db
    var i = 0
    db.stream(streamingLogTable.result.withStatementParameters(fetchSize = 100,
      rsType = ResultSetType.ForwardOnly,
      rsConcurrency = ResultSetConcurrency.ReadOnly).transactionally).foreach {
      a => {
        println(a)
      }
    }
  }


  def main(args: Array[String]) {
    val db = DatabaseWrapper.db
    createDb()
    bootstrapData()

    Thread.sleep(1000)
    printAll()
    Thread.sleep(10000000)
  }
}

