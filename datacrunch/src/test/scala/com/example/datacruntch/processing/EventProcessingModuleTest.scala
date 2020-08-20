package com.example.datacruntch.processing

import java.text.SimpleDateFormat

import com.example.datacruntch.storage.EventStorageModule
import org.scalatest.{FlatSpec, BeforeAndAfterEach, ShouldMatchers}

import scala.util.Try

class EventProcessingModuleTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers {
  "valid event log line" should "be able to be parsed to event object" in {
    val eventProcessing = createSystem()
    val logLine = "04-08-2015-14:22 1 ARCHIVED"
    val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
    val eventDate = df.parse("04-08-2015-14:22")
    implicit val source = "service1"
    val event = eventProcessing.parseEvent(logLine).get
    event.eventType should equal("ARCHIVED")
    event.date should equal(eventDate)
  }

  "Multiple log lines" should "be ablt to be parsed to event obj list" in {
    val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
    val logLines = Iterator(
      "04-08-2015-14:22 1 ARCHIVED",
      "04-08-2015-15:22 1 ARCHIVED",
      "04-08-2015-10:22 1 ARCHIVED",
      "05-08-2015-14:22 1 ARCHIVED",
      "05-08-2015-14:22 1 ARCHIVED"
    )

    val eventProcessing = createSystem()
    val events = eventProcessing.parseEvents(logLines)("DDD")
    events should have size 5
  }


  "Malformed log entries" should "be skipped and rest should be parsed" in {
    val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
    val logLines = Iterator(
      "asfd-08-2015-14:22 1 ARCHIVED",
      "01-08-2015-14:22 1 ARCHIVED",
      "01-08-2015-14:22 1 ARCHIVED",
      "01-08-2015-14:22 1 ARCHIVED",
      "01-08-2015-14:22 1 ARCHIVED",
      "05-08-2015-14:22 1 ARCHIVED"
    )

    val eventProcessing = createSystem()
    val events = eventProcessing.parseEvents(logLines)("DDD").to[List]
    events should have size 6 //entire list get parsed and returned in Try monad.
    events.flatMap(_.toOption) should have size 5 //we unwrap the content, to remove Failures.

  }

  def createSystem() = new DocumentEventProcessingModule with EventStorageModule with MonthlyDocumentEventAggregationAlg {

    override type AggregatedEvent = AggregatedDocumentEvent


    var backend: List[AggregatedDocumentEvent] = List()


    override def store(events: Iterable[AggregatedDocumentEvent]): Try[Unit] = ???

    override def loadAll(): Try[List[AggregatedDocumentEvent]] = ???

    override def store(event: AggregatedDocumentEvent): Unit = {
      backend = event :: backend
    }
  }
}
