package com.example.datacruntch.processing

import java.util.UUID

import akka.event.slf4j.SLF4JLogging
import com.example.datacruntch.TestData
import com.example.datacruntch.storage.cassandra.{BootstrappableCassandraStorageModule, CassandraConnection}

import org.scalatest.{FlatSpec, ShouldMatchers}

class CassandraIntegrationTest extends FlatSpec with CassandraConnection with TestData with ShouldMatchers with SLF4JLogging {
  val system = new DocumentEventProcessingModule with BootstrappableCassandraStorageModule with MonthlyDocumentEventAggregationAlg {
    override def store(event: AggregatedEvent): Unit = {
      val e =
        if (event.eventType === "EXCEPTION") new AggregatedEvent(null, null, null, 1) //this will cause a exception in cassandra.
        else event
      super.store(e)
    }
  }

  "System" should "properly process log files and store events" in {


    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(f => !f.getName.contains("malformed") && !f.getName.contains("backend_exception"))

    system.bootstrap(List())
    system.processLogDir(logFilesDir)
    system.loadAll().isSuccess shouldBe true
    system.loadAll().get.size shouldBe 808
    listFiles(logFilesDir)(_.isFile).length shouldBe 0
  }

  "When there are errors in logfile, those errors" should "be skipped and rest of the file should be processed" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID

    prepare(logFilesDir)(!_.getName.contains("backend_exception"))
    system.bootstrap(List())
    system.processLogDir(logFilesDir)
    system.loadAll().isSuccess shouldBe true
    system.loadAll().get.size shouldBe 810
    listFiles(logFilesDir)(_.isFile).length shouldBe 0
  }

  "When there are backend erros, processing of the file" should "be stopped" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID

    prepare(logFilesDir)(_.getName.contains("backend_exception"))
    system.bootstrap(List())
    system.processLogDir(logFilesDir)
    system.loadAll().isSuccess shouldBe true
    system.loadAll().get.size shouldBe 3
    listFiles(logFilesDir)(_.isFile).length shouldBe 1
  }

  "Insert operations" should "be idempotent" in {
    var logFilesDir = "/tmp/" + UUID.randomUUID

    prepare(logFilesDir)(_.getName.contains("02-10-2015.txt"))
    system.bootstrap(List())
    system.processLogDir(logFilesDir)
    system.loadAll().isSuccess shouldBe true
    system.loadAll().get.size shouldBe 8
    listFiles(logFilesDir)(_.isFile).length shouldBe 0

    //re-run the same
    logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(_.getName.contains("02-10-2015.txt"))
    system.processLogDir(logFilesDir)
    system.loadAll().isSuccess shouldBe true
    system.loadAll().get.size shouldBe 8
    listFiles(logFilesDir)(_.isFile).length shouldBe 0
  }
}
