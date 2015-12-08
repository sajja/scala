package com.example.datacruntch.processing

import java.io.File
import java.util.{Date, UUID}

import akka.event.slf4j.SLF4JLogging
import com.example.datacruntch.TestData
import com.example.datacruntch.storage.EventStorageModule
import com.example.datacruntch.storage.cassandra.{BootstrapedCassandraEventStorageModule, CassandraConnection}
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, ShouldMatchers}
import org.apache.log4j.{Priority, Logger}
;

import scala.util.Try

/**
  * Created by sajith on 11/14/15.
  */
class CassandraIntegrationTest extends FlatSpec with CassandraConnection with TestData with ShouldMatchers with SLF4JLogging {

  trait DummyStorageModule extends EventStorageModule {
    override type DomainObject = Event

    override def store(listOf: List[Event]): Try[Unit] = {
      Try(listOf.foreach(store))
    }

    override def store(e: Event): Unit = {
    }

    override def bootstrap(events: List[Event]): Try[Unit] = Try(List())

    override def loadByDate(date: Date): List[Event] = List()
  }

  implicit val source = "service1"

  val config = ConfigFactory.load()
  val dateFormat = config.getString("log.dateFormat")

  "Application" should "be able to store processed data to cassandra backend" in {
    var count = 0
    object application extends DataProcessingModule with EventProcessingAlgorithms with FailableCassandraStorage

    trait FailableCassandraStorage extends BootstrapedCassandraEventStorageModule {
      override def store(e: Event): Unit = {
        if (e.event === "THROW_ERROR") {
          throw new Exception("Storage error")
        }
        else {
          count += 1
          super.store(e)
        }
      }

    }
    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(_.isFile)
    val logDir = new File("tmp").getAbsolutePath
    val algos = application.algo
    val files = listFiles(logFilesDir)(_.isFile)

    application.bootstrap(List())
    application.LogProcessingService.processLogs(logFilesDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    application.countAll() should equal(11404)
  }
}
