package com.example.datacruntch

import com.example.datacruntch.processing.{DataProcessingModule, DomainModelModule, EventProcessingAlgorithms}
import com.example.datacruntch.storage.cassandra.BootstrapedCassandraEventStorageModule

import scala.util.Try


object DataProcessingApplication extends DataProcessingModule with EventProcessingAlgorithms with BootstrapedCassandraEventStorageModule {

}

object Test extends DomainModelModule {
  def fn(i: Int) = if (i == 2) throw new Exception

  def main(args: Array[String]): Unit = {
    //    val logProcessingService = DataProcessingApplication.LogProcessingService
    //    val processingAlgorhyms = DataProcessingApplication.algo
    //    DataProcessingApplication.bootstrap(List())
    //    implicit val soruce = "xxx_yyy"
    //    logProcessingService.processLogs("/home/sajith/scratch/scala/datacrunch/tmp", "MM-dd-yyyy-HH:mm")(processingAlgorhyms.mapper)(processingAlgorhyms.reduce)(processingAlgorhyms.convert)
    val l = List(1, 2, 4)
    val j = Try(l.foreach(fn))
    println(j.isSuccess)
  }
}
