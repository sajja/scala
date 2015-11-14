package com.example.datacruntch

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.example.datacruntch.processing.{DataProcessingModule, DomainModelModule, EventProcessingAlgorithms}
import com.example.datacruntch.storage.{EventStorageModule, FileSystemModule}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec, ShouldMatchers}

import scala.util.Try

class DataProcessingTest extends FlatSpec with BeforeAndAfterEach with ShouldMatchers with DomainModelModule with FileSystemModule {

  val config = ConfigFactory.load()
  val dateFormat = config.getString("log.dateFormat")
  val df = new SimpleDateFormat(dateFormat)
  implicit val source = "filename_servicename"

  def prepare(f: (File => Boolean)): Unit = {
    val testLogLocation = new File("testlogs")
    if (!testLogLocation.exists()) throw new Exception("Cannot locate test logs")
    if (!testLogLocation.isDirectory) throw new Exception("test log location is not a directory")

    val workDir = new File("tmp")
    if (!workDir.exists()) workDir.mkdir()
    listFiles(workDir.getAbsolutePath)(_.isFile).foreach {
      case file: File => file.delete()
    }

    testLogLocation.listFiles().filter(f).foreach {
      case file: File => cp(file, workDir.getAbsolutePath, file.getName)
    }
  }

  "ETL operation" should "not fail when the inut has invalid data" in {
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with DummyStorageModule

    val expectedKeys = List(
      (new dataProcessingModule.Key(df.parse("01-08-2015-01:00"), 1, source, "ROUTED"), 1),
      (new dataProcessingModule.Key(df.parse("01-08-2015-04:00"), 1, source, "UPDATE"), 1),
      (new dataProcessingModule.Key(df.parse("03-08-2015-04:00"), 1, source, "ROUTED"), 1),
      (new dataProcessingModule.Key(df.parse("01-08-2015-03:00"), 1, source, "UPDATE"), 2),
      (new dataProcessingModule.Key(df.parse("02-08-2015-04:00"), 1, source, "ROUTED"), 1)
    )

    val algos = dataProcessingModule.algo
    val logStream = getClass.getResourceAsStream("/log_with_malformed_entries.txt")
    val extactedData = dataProcessingModule.LogProcessingService.extract(logStream, df)(algos.mapper)(algos.reduce)
    extactedData.isSuccess should equal(true)
    extactedData.foreach((keyToInt: Map[dataProcessingModule.Key, Int]) => keyToInt.foreach((tuple: (dataProcessingModule.Key, Int)) => println(tuple._1 + " " + tuple._2)))
    extactedData.get.size should equal(5)

    expectedKeys.foreach {
      case (k: dataProcessingModule.Key, v: Int) =>
        extactedData.get.get(k).get should equal(v)
    }
  }


  "Extraction method" should "be able to process a input steam and extract data" in {
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with DummyStorageModule
    val algos = dataProcessingModule.algo
    val logStream = getClass.getResourceAsStream("/01-08-2015.txt")
    val extactedData = dataProcessingModule.LogProcessingService.extract(logStream, df)(algos.mapper)(algos.reduce)
    extactedData.isSuccess should equal(true)
    extactedData.get.size should equal(6)
  }

  "Application" should "able to process files in dir and create events." in {
    prepare(!_.getName.contains("error.txt"))
    var count = 0
    trait CountingDummyStorage extends DummyStorageModule {
      override def store(e: Event): Unit = {
        count += 1
      }
    }
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with CountingDummyStorage
    val algos = dataProcessingModule.algo
    val logDir = new File("tmp").getAbsolutePath
    dataProcessingModule.LogProcessingService.processLogs(logDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    count should equal(11386)
  }


  "When encountered backend error, application" should "partially store data and exit, but not remove the file in err" in {
    prepare(!_.getName.contains("content_error.txt"))
    var count = 0
    trait FailableDummyStorage extends DummyStorageModule {
      override def store(e: Event): Unit = {
        if (e.event === "THROW_ERROR") throw new Exception("Simulated backend error")
        else count += 1
      }
    }
    val dataProcessingModule = new DataProcessingModule with EventProcessingAlgorithms with FailableDummyStorage
    val algos = dataProcessingModule.algo
    val logDir = new File("tmp").getAbsolutePath
    dataProcessingModule.LogProcessingService.processLogs(logDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    count should equal(11389)
    val files = listFiles(logDir)(_.isFile)
    files.length should equal(1)
    files.head.getName should equal("storage_error.txt")
  }

  trait DummyStorageModule extends EventStorageModule {
    override type DomainObject = Event

    override def store(listOf: Iterable[Event]): Int = {
      listOf.foreach(store)
      listOf.size
    }

    override def store(e: Event): Unit = {
    }

    override def bootstrap(events: List[Event]): Try[Unit] = Try(List())

    override def loadByDate(date: Date): List[Event] = List()
  }

}
