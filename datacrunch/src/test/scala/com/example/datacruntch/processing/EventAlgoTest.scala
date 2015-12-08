package com.example.datacruntch.processing

import java.text.SimpleDateFormat
import java.util.Date

import org.scalatest.{BeforeAndAfterEach, FlatSpec, ShouldMatchers}

class EventAlgoTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers with DomainModelModule {

  val algorithmModule = new EventProcessingAlgorithms {}
  val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
  implicit val source = "filename_servicename"
  val logLine = "04-08-2015-14:22 1 ARCHIVED"

  "Map algorithm" should "be a failure when it hits unparsable date" in {
    val logLine = "d4-08-2015-14:22 1 ARCHIVED"
    algorithmModule.algo.mapper(logLine, source).isFailure should equal(true)
  }

  "Map algorithm" should "create a event from a log line" in {
    val (event, count) = algorithmModule.algo.mapper(logLine, source).get

    df.format(event.date) should equal("04-08-2015-14:00")
    event.customerId should equal(1)
    event.event should equal("ARCHIVED")
    event.inputSource should equal("filename_servicename")
    count should equal(1)
  }

  "Reduce algorithm" should "aggregate records with same key" in {
    val result: Map[algorithmModule.Key, Int] = algorithmModule.algo.reduce(dataSet.iterator.map(l => algorithmModule.algo.mapper(l, source)))
    result.size should equal(keySet.size)
    keySet foreach {
      case (k, v) =>
        result.get(k) match {
          case Some(i) => v should equal(i)
          case None => fail(s"$k should be in aggregation")
        }
    }
  }

  "Reduce algorithm" should "be able to filter garbage data in the input" in {
    val dataSet = Seq(
      "02-08-2015-11:22 1 ARCHIVED",
      "02-08-2015-11:22 1 ARCHIVED",
      "02-vv-2015-11:22 1 ARCHIVED",
      "02-08-2015-11:22 hh ARCHIVED"
    )
    val result = algorithmModule.algo.reduce(dataSet.iterator.map(l => algorithmModule.algo.mapper(l, source)))
    result.size should equal(1)
  }


  "Transform algorithm" should "transform the key->int to an Event" in {
    val date = new Date()
    val event: algorithmModule.Event = algorithmModule.algo.convert(new algorithmModule.Key(date, 1, source, "test"), 1)
    event.date should equal(date)
    event.customerId should equal(1)
  }

  /** distribution of the dataset
    * C-1 - 02-08-2015-22 ARCHIVED - 2
    * C-1 - 02-08-2015-22 UPDATE - 1
    * C-1 - 02-09-2015-12 UPDATE - 2
    * C-1 - 02-08-2015-11 ROUTED - 2
    * C-1 - 02-08-2015-11 ARCHIVED -1
    * C-1 - 02-09-2015-12 ARCHIVED - 2
    * C-2 - 02-08-2015-22 UPDATE - 1
    * C-2 - 02-08-2015-22 ARCHIVED - 2
    * C-2 - 02-08-2015-11 ARCHIVED - 1
    * C-2 - 02-08-2015-11 ROUTED - 2
    * C-4 - 02-08-2015-11 UPDATE - 2
    * C-4 - 02-10-2015-04 ARCHIVED -4
    * C-4 - 02-10-2015-04 UPDATE -1
    */
  val dataSet = Seq(
    "02-08-2015-11:22 1 ARCHIVED",
    "02-08-2015-22:10 1 ARCHIVED",
    "02-08-2015-22:11 1 ARCHIVED",
    "02-08-2015-11:32 1 ROUTED",
    "02-08-2015-11:03 1 ROUTED",
    "02-08-2015-22:20 1 UPDATE",
    "02-09-2015-12:02 1 UPDATE",
    "02-09-2015-12:52 1 UPDATE",
    "02-09-2015-12:48 1 ARCHIVED",
    "02-09-2015-12:46 1 ARCHIVED",
    "02-08-2015-22:48 2 UPDATE",
    "02-08-2015-11:11 2 ARCHIVED",
    "02-08-2015-22:10 2 ARCHIVED",
    "02-08-2015-22:17 2 ARCHIVED",
    "02-08-2015-11:42 2 ROUTED",
    "02-08-2015-11:38 2 ROUTED",
    "02-10-2015-04:31 4 ARCHIVED",
    "02-10-2015-04:01 4 ARCHIVED",
    "02-10-2015-04:50 4 ARCHIVED",
    "02-10-2015-04:37 4 ARCHIVED",
    "02-08-2015-11:28 4 UPDATE",
    "02-08-2015-11:08 4 UPDATE",
    "02-10-2015-04:34 4 UPDATE"
  )

  val keySet = Map(
    new algorithmModule.Key(df.parse("02-08-2015-22:00"), 1, source, "ARCHIVED") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-22:00"), 1, source, "UPDATE") -> 1,
    new algorithmModule.Key(df.parse("02-09-2015-12:00"), 1, source, "UPDATE") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-11:00"), 1, source, "ROUTED") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-11:00"), 1, source, "ARCHIVED") -> 1,
    new algorithmModule.Key(df.parse("02-09-2015-12:00"), 1, source, "ARCHIVED") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-22:00"), 2, source, "UPDATE") -> 1,
    new algorithmModule.Key(df.parse("02-08-2015-22:00"), 2, source, "ARCHIVED") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-11:00"), 2, source, "ARCHIVED") -> 1,
    new algorithmModule.Key(df.parse("02-08-2015-11:00"), 2, source, "ROUTED") -> 2,
    new algorithmModule.Key(df.parse("02-08-2015-11:00"), 4, source, "UPDATE") -> 2,
    new algorithmModule.Key(df.parse("02-10-2015-04:00"), 4, source, "ARCHIVED") -> 4,
    new algorithmModule.Key(df.parse("02-10-2015-04:00"), 4, source, "UPDATE") -> 1
  )
}
