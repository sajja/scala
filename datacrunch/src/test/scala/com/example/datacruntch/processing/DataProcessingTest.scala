package com.example.datacruntch.processing

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.example.datacruntch.TestData
import com.example.datacruntch.storage.EventStorageModule
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec, ShouldMatchers}

import scala.util.Try

class DataProcessingTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers with DomainModelModule with TestData {

  val config = ConfigFactory.load()
  val dateFormat = config.getString("log.dateFormat")
  val df = new SimpleDateFormat(dateFormat)
  implicit val source = "filename_servicename"


  "ETL operation" should "not fail when the inut has invalid data" in {
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with DummyStorageModule
    //
    val expectedKeys = List(
      (new dataProcessingModule.Key(df.parse("01-08-2015-01:00"), 1, source, "ROUTED"), 1),
      (new dataProcessingModule.Key(df.parse("01-08-2015-04:00"), 1, source, "UPDATE"), 1),
      (new dataProcessingModule.Key(df.parse("03-08-2015-04:00"), 1, source, "ROUTED"), 1),
      (new dataProcessingModule.Key(df.parse("01-08-2015-03:00"), 1, source, "UPDATE"), 2),
      (new dataProcessingModule.Key(df.parse("02-08-2015-04:00"), 1, source, "ROUTED"), 1)
    )

    val algos = dataProcessingModule.algo
    val logStream = getClass.getResourceAsStream("/log_with_malformed_entries.txt")
    val extactedData = dataProcessingModule.LogProcessingService.extract(logStream, df)(algos.mapper)(algos.reduce)(source)
    extactedData.isSuccess should equal(true)
    extactedData.get.size should equal(5)

    extactedData.get.foreach((tuple: (dataProcessingModule.Key, Int)) => println(tuple._1))

    expectedKeys.foreach {
      case (k: dataProcessingModule.Key, v: Int) =>
        println("x->" + k)
        extactedData.get.get(k).get should equal(v)
    }
  }


  "Extraction method" should "be able to process a input steam and extract data" in {
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with DummyStorageModule
    val algos = dataProcessingModule.algo
    val logStream = getClass.getResourceAsStream("/01-08-2015.txt")
    val extactedData = dataProcessingModule.LogProcessingService.extract(logStream, df)(algos.mapper)(algos.reduce)("service")
    extactedData.isSuccess should equal(true)
    extactedData.get.size should equal(6)
  }

  "Application" should "able to process files in dir and create events." in {
    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(!_.getName.contains("error.txt"))
    var count = 0
    trait CountingDummyStorage extends DummyStorageModule {
      override def store(e: Event): Unit = {
        count += 1
      }
    }
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with CountingDummyStorage
    val algos = dataProcessingModule.algo
    val logDir = new File("tmp").getAbsolutePath
    dataProcessingModule.LogProcessingService.processLogs(logFilesDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    count should equal(11386)
  }

  "When encountered backend error, application" should "partially store data and exit, but not remove the file in err" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(_.isFile)
    var count = 0
    trait FailableDummyStorage extends DummyStorageModule {
      override def store(e: Event): Unit = {
        if (e.event === "THROW_ERROR") throw new Exception("Simulated backend error")
        else count += 1
      }
    }
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with FailableDummyStorage
    val algos = dataProcessingModule.algo
    dataProcessingModule.LogProcessingService.processLogs(logFilesDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    count should equal(11406)

    val files = listFiles(logFilesDir)(_.isFile)
    files.length should equal(1)
    files.head.getName should equal("storage_error.txt")
  }


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

}
