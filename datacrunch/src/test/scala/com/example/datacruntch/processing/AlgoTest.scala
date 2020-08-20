package com.example.datacruntch.processing

import java.text.SimpleDateFormat
import java.util.Date

import org.scalatest._

import scala.util.Try

class AlgoTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers with DomainObjects {

  val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")

  val alg = new Algorhyms with MonthlyDocumentEventAggregationAlg {
    def createEvent(key: String, customer: String, event: String, date: Date, source:String) = new DocumentEvent(customer, event, date, source)
  }

  "Aggregation" should "be monthly" in {
    val dataSet = createEventDataset1()
    val triedMappedEvents = alg.map(dataSet)
    val reduce = alg.reduce(triedMappedEvents)
    reduce.size shouldBe 16

    val cust1Events = reduce.filter { case (_, event) => event.customer === "1" }
    val cust2Events = reduce.filter { case (_, event) => event.customer === "2" }

    filterByEvent(cust1Events, "CREATE") should have size 3
    countEvents(cust1Events, "CREATE") shouldBe 7
    filterByEvent(cust1Events, "WSI") should have size 2

    filterByEvent(cust1Events, "DELIVERED") should have size 3
    countEvents(cust1Events, "DELIVERED") shouldBe 6

    filterByEvent(cust2Events, "WSI") should have size 1
    countEvents(cust2Events, "WSI") shouldBe 1

    println(filterByEvent(cust1Events, "CREATE").keySet)
    filterByEvent(cust1Events, "CREATE").keySet & Set("05-01-2015_1_CREATE", "06-01-2015_1_CREATE", "04-01-2015_1_CREATE") should have size 3
    //can do more tests here......

  }

  def filterByEvent(events: Map[String, AlgoTest.this.alg.DocumentEvent], event: String) = events.filter { case (_, e) => e.eventType === event }

  def countEvents(events: Map[String, AlgoTest.this.alg.DocumentEvent], event: String) = filterByEvent(events, event).foldLeft(0) {
    case (i, tuple) => tuple._2.count + i
  }

  "Raw events" should "transformed to {date_rounded_by_month_customer}_{eventType}" in {
    val expectedValues = Seq(
      s"04-01-2015_1_CREATE",
      s"04-01-2015_1_ROUTED",
      s"04-01-2015_1_DELIVERED",
      s"04-01-2015_1_CREATE",
      s"04-01-2015_1_ROUTED",
      s"04-01-2015_1_DELIVERED",
      s"05-01-2015_1_CREATE",
      s"05-01-2015_1_STOPPED",
      s"06-01-2015_2_DELIVERED",
      s"10-01-2015_2_W3I",
      s"11-01-2015_2_STOPPED"
    )

    val dataset = createEventDataset()
    val mappedEvents = alg.map(dataset).to[Seq]
    mappedEvents should have size expectedValues.size

    compare(mappedEvents.head, expectedValues.head)
    compare(mappedEvents(1), expectedValues(1))
    compare(mappedEvents(2), expectedValues(2))
    compare(mappedEvents(3), expectedValues(3))
    compare(mappedEvents(4), expectedValues(4))
    compare(mappedEvents(5), expectedValues(5))
    compare(mappedEvents(6), expectedValues(6))
    compare(mappedEvents(7), expectedValues(7))
    compare(mappedEvents(8), expectedValues(8))
    compare(mappedEvents(9), expectedValues(9))
    compare(mappedEvents(10), expectedValues(10))
  }

  def compare(actual: Try[(String, AlgoTest.this.alg.Event)], expected: String) = {
    actual.get._1 should equal(expected)
  }

  private def createEventDataset1() = {
    Iterator(
      Try(alg.createEvent("", "1", "CREATE", df.parse("04-08-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "WSI", df.parse("04-08-2015-14:24"),"S1")),

      Try(alg.createEvent("", "1", "CREATE", df.parse("04-09-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("04-09-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("04-09-2015-14:22"),"S1")),


      Try(alg.createEvent("", "1", "CREATE", df.parse("04-20-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("04-20-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("04-20-2015-14:22"),"S1")),

      Try(alg.createEvent("", "2", "CREATE", df.parse("04-09-2015-15:22"),"S1")),
      Try(alg.createEvent("", "2", "ROUTED", df.parse("04-09-2015-14:22"),"S1")),
      Try(alg.createEvent("", "2", "DELIVERED", df.parse("04-09-2015-14:22"),"S1")),

      Try(alg.createEvent("", "1", "CREATE", df.parse("05-20-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("05-20-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("05-20-2015-14:22"),"S1")),

      Try(alg.createEvent("", "1", "CREATE", df.parse("05-20-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("05-20-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("05-20-2015-14:22"),"S1")),

      Try(alg.createEvent("", "1", "CREATE", df.parse("05-21-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("05-21-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("05-21-2015-14:22"),"S1")),

      Try(alg.createEvent("", "2", "CREATE", df.parse("05-21-2015-15:22"),"S1")),
      Try(alg.createEvent("", "2", "WSI", df.parse("05-21-2015-14:22"),"S1")),

      Try(alg.createEvent("", "1", "WSI", df.parse("05-21-2015-14:22"),"S1")),

      Try(alg.createEvent("", "1", "CREATE", df.parse("06-20-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("06-20-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("06-20-2015-14:22"),"S1"))
    )
  }

  private def createEventDataset() = {
    Iterator(
      Try(alg.createEvent("", "1", "CREATE", df.parse("04-08-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("04-08-2015-14:23"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("04-08-2015-14:24"),"S1")),
      Try(alg.createEvent("", "1", "CREATE", df.parse("04-08-2015-15:22"),"S1")),
      Try(alg.createEvent("", "1", "ROUTED", df.parse("04-05-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "DELIVERED", df.parse("04-05-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "CREATE", df.parse("05-06-2015-14:22"),"S1")),
      Try(alg.createEvent("", "1", "STOPPED", df.parse("05-06-2015-14:22"),"S1")),
      Try(alg.createEvent("", "2", "DELIVERED", df.parse("06-13-2015-16:22"),"S1")),
      Try(alg.createEvent("", "2", "W3I", df.parse("10-13-2015-14:22"),"S1")),
      Try(alg.createEvent("", "2", "STOPPED", df.parse("11-14-2015-14:22"),"S1"))
    )
  }
}
