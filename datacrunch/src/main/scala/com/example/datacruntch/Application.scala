package com.example.datacruntch

import com.example.datacruntch.processing.{DataProcessingModule, DomainModelModule, EventProcessingAlgorithms}
import com.example.datacruntch.storage.cassandra.BootstrapedCassandraEventStorageModule


object DataProcessingApplication extends DataProcessingModule with EventProcessingAlgorithms with BootstrapedCassandraEventStorageModule


object Test extends DomainModelModule {
  def main(args: Array[String]): Unit = {
    val logProcessingService = DataProcessingApplication.LogProcessingService
    val processingAlgorhyms = DataProcessingApplication.algo
    DataProcessingApplication.bootstrap(List())
    implicit val soruce = "xxx_yyy"
    logProcessingService.processLogs("/home/sajith/scratch/scala/datacrunch/tmp", "MM-dd-yyyy-HH:mm")(processingAlgorhyms.mapper)(processingAlgorhyms.reduce)(processingAlgorhyms.convert)
  }
}
