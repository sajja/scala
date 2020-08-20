package com.example.datacruntch.processing

import java.util.{Date, UUID}

import com.example.datacruntch
import com.example.datacruntch.storage.EventStorageModule
import org.scalatest._

import scala.util.Try

class EventProcessingIntegrationTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers with datacruntch.TestData {

  "System" should "be able to process log files store and remove the files" in {

    val system = createSystem()

    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(f => !f.getName.contains("malformed") && !f.getName.contains("backend_exception"))
    system.processLogDir(logFilesDir)
    system.backend.size shouldBe 808

    listFiles(logFilesDir)(_.isFile).length shouldBe 0
  }

  "System" should "be able to process log files with errors without crashing" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID

    val system = createSystem()
    prepare(logFilesDir)(!_.getName.contains("backend_exception"))

    system.processLogDir(logFilesDir)

    system.backend.size shouldBe 810
    listFiles(logFilesDir)(_.isFile).length shouldBe 0
  }

  "When storage error happens, System" should "stop processing the file and move on. It should not remove file" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID
    val system = createSystem()

    prepare(logFilesDir)(_.getName.contains("backend_exception"))
    system.processLogDir(logFilesDir)
    system.backend.size shouldBe 3
    val files = listFiles(logFilesDir)(_.isFile)

    files should have size 1
    files.head.getName shouldBe "backend_exception.txt"
  }



  trait X extends MonthlyDocumentEventAggregationAlg {
    def createEvent(customer: String, event: String, date: Date, source: String) = new DocumentEvent(customer, event, date, source)
  }


  def createSystem() = new DocumentEventProcessingModule with EventStorageModule with MonthlyDocumentEventAggregationAlg {
    override type AggregatedEvent = AggregatedDocumentEvent

    var backend: List[ AggregatedDocumentEvent] = List()

    override def loadAll():Try[List[AggregatedDocumentEvent]] = ???

    override def store(events: Iterable[AggregatedDocumentEvent]): Try[Unit] = Try(for (x <- events) store(x))


    override def store(event: AggregatedDocumentEvent): Unit =
      if (event.eventType === "EXCEPTION") throw new Exception("Simulated exception")
      else backend = event :: backend
  }
}
