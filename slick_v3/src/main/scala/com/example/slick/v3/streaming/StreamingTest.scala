package com.example.slick.v3.streaming

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

  class StreamingLog(tag: Tag) extends Table[(Int, String)](tag, "streaming_log") {
    def id = column[Int]("id", O.PrimaryKey)

    // This is the primary key column
    def log = column[String]("log")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, log)
  }

  val streamingLogTable = TableQuery[StreamingLog]


  def bootstrap() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      streamingLogTable.schema.drop,
      streamingLogTable.schema.create,
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4)),
      streamingLogTable += (Random.nextInt(10000), Random.nextString(4))
    )

    //    val setupFuture = db.run(setup)
    //    Await.result(setupFuture, 10 second)
  }

  def stream(): StreamingDBIO[Seq[(Int)], (Int)] = {
    val reslutls = streamingLogTable.map((log: StreamingLog) => log.id).result
    reslutls
  }

  def printAll() = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val db = DatabaseWrapper.db
    //    println("XXXXXXXXXXXXXXXX")
    //
    //    val pub: DatabasePublisher[Int] = db.stream(stream())
    //    pub.foreach {
    //      a =>
    //        println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
    //        Thread.sleep(100)
    //        println(a)
    //    }


    //    Await.result(db.run(streamingLogTable.result),Duration.Inf).foreach(println)


    db.stream(streamingLogTable.result.withStatementParameters(fetchSize = 100,
      rsType = ResultSetType.ForwardOnly,
      rsConcurrency = ResultSetConcurrency.ReadOnly).transactionally).foreach {
      a => {
        println(a)
        Thread.sleep(1000)
      }
    }
  }


  def main(args: Array[String]) {
    val db = DatabaseWrapper.db
    bootstrap()
    printAll()
  }
}

