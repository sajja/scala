package com.example.datacruntch.processing

import java.io.{File, FileInputStream}
import java.text.SimpleDateFormat
import java.util.Calendar

import com.example.datacruntch.storage.EventStorageModule

import scala.io.Source
import scala.util.Try


trait Algorhyms extends DomainObjects {
  def map(events: Iterator[Try[Event]]): Iterator[Try[(String, Event)]]

  def reduce(rec: Iterator[Try[(String, Event)]]): Map[String, Event]
}

trait MonthlyDocumentEventAggregationAlg extends Algorhyms {
  val df = new SimpleDateFormat("MM-dd-yyyy")

  override def map(events: Iterator[Try[DocumentEvent]]): Iterator[Try[(String, Event)]] = {
    events map {
      tryEvent => {
        for {
          event <- tryEvent
        } yield {
          val cal = Calendar.getInstance()
          cal.setTime(event.date)
          cal.set(Calendar.MILLISECOND, 0)
          cal.set(Calendar.MINUTE, 0)
          cal.set(Calendar.SECOND, 0)
          cal.set(Calendar.HOUR_OF_DAY, 0)
          cal.set(Calendar.DAY_OF_MONTH, 1)
          val rounded = cal.getTime
          (s"${df.format(rounded)}_${event.customer}_${event.eventType}", event)
        }
      }
    }
  }

  override def reduce(triedMappedList: Iterator[Try[(String, DocumentEvent)]]): Map[String, DocumentEvent] = {
    triedMappedList.foldLeft(Map[String, Event]()) { (agg: Map[String, Event], tuple: Try[(String, Event)]) =>
      if (tuple.isSuccess) {
        val (key, event) = tuple.get
        agg.get(key) match {
          case Some(a) => agg ++ Map(key -> new DocumentEvent(a.customer, a.eventType, a.date, a.count + 1, a.source))
          case None => agg ++ Map(key -> new DocumentEvent(event.customer, event.eventType, event.date, event.source))
        }
      } else agg
    }
  }

  override type Event = DocumentEvent

}


trait EventProcessingModule extends DomainObjects {
  def parseEvent(eventRecord: String)(implicit source: String): Try[Event]

  def parseEvents(eventRecords: Iterator[String])(implicit source: String): Iterator[Try[DocumentEvent]]

  def processEvents(events: Iterator[Try[Event]]): Map[String, Event]

  def processLogDir(dir: String)
}

trait DocumentEventProcessingModule extends EventProcessingModule with FileSystemModule {
  this: EventStorageModule with Algorhyms =>

  override type AggregatedEvent = AggregatedDocumentEvent
  override type Event = DocumentEvent

  val df1 = new SimpleDateFormat("MM-dd-yyyy-HH:mm")


  def loadFile(file: File) = Try(new FileInputStream(file))

  override def processLogDir(dir: String): Unit = {
    val files = listFiles(dir)(_.isFile)
    files.foreach {
      file => {
        val events = parseEvents(toString(file))(file.getName)
        val processedEvents = processEvents(events)
        for {
          _ <- store(processedEvents.map(
            r => {
              new AggregatedDocumentEvent(r._1, r._2.eventType, r._2.source, r._2.count)
            }))
        } yield rm(file)
      }
    }
  }

  override def processEvents(events: Iterator[Try[DocumentEvent]]): Map[String, Event] = {
    reduce(map(events))
  }

  def toString(file: File) = Source.fromInputStream(new FileInputStream(file)).getLines()

  override def parseEvents(eventRecords: Iterator[String])(implicit source: String): Iterator[Try[DocumentEvent]] = eventRecords map parseEvent

  override def parseEvent(eventRecord: String)(implicit source: String): Try[DocumentEvent] = {
    Try {
      val tokens = eventRecord.split(" ")
      val calendar = Calendar.getInstance()
      val date = df1.parse(tokens(0))
      calendar.setTime(date)
      calendar.set(Calendar.MINUTE, 0)

      val hourRoundedDate = calendar.getTime
      calendar.set(Calendar.HOUR_OF_DAY, 0)
      val datyRoundedDate = calendar.getTime
      new DocumentEvent(tokens(1), tokens(2), date, source)
    }
  }
}
