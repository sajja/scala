package com.example.datacruntch.reporting

import java.util.{Calendar, UUID}

import com.example.datacruntch.TestData
import com.example.datacruntch.processing.{DataProcessingModule, EventDataReportingModule, EventProcessingAlgorithms}
import com.example.datacruntch.storage.cassandra.{BootstrapedCassandraEventStorageModule, CassandraConnection, CassandraEventStorageModule}
import com.typesafe.config.ConfigFactory
import org.scalatest._

/**
  * Created by sajith on 11/16/15.
  */
class CassandraReportingTest extends FlatSpec with CassandraConnection with TestData with ShouldMatchers {
  val config = ConfigFactory.load()
  val dateFormat = config.getString("log.dateFormat")

  object processingModule extends DataProcessingModule with EventProcessingAlgorithms with BootstrapedCassandraEventStorageModule

  val algos = processingModule.algo

  object reportingModule extends EventDataReportingModule with CassandraEventStorageModule {
  }

  implicit val source = "sevrice1"
  "" should "" in {
    val logFilesDir = "/tmp/" + UUID.randomUUID
    prepare(logFilesDir)(_.getName.contains("01-08-2015"))
    processingModule.bootstrap(List())
    processingModule.LogProcessingService.processLogs(logFilesDir, dateFormat)(algos.mapper)(algos.reduce)(algos.convert)
    val start = getRoundadDate(2015,1,8,1)
    val end = getRoundadDate(2015,1,9,22)

    val res = reportingModule.load(start,end).sorted
    res.foreach(e=> println(s"${e.date} ${e.customerId} ${e.event}"))

    res.nonEmpty should equal(true)
  }


  def getRoundadDate(year: Int, month: Int, day: Int, hour: Int) = {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DATE, day)
    cal.set(Calendar.HOUR, hour)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.getTime
  }

}
