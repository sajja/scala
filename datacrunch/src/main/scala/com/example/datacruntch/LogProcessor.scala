package com.example.datacruntch

import com.example.datacruntch.processing.{DocumentEventProcessingModule, MonthlyDocumentEventAggregationAlg}
import com.example.datacruntch.storage.cassandra.BootstrappableCassandraStorageModule

object LogProcessor {
  def main(args: Array[String]) {
     val app = new DocumentEventProcessingModule with BootstrappableCassandraStorageModule with MonthlyDocumentEventAggregationAlg {
      override def store(event: AggregatedEvent): Unit = {
        val e =
          if (event.eventType == "EXCEPTION") new AggregatedEvent(null, null, null, 1) //this will cause a exception in cassandra.
          else event
        super.store(e)
      }
    }

    app.bootstrap(List())

    app.processLogDir("/home/sajith/scratch/scala/datacrunch/logs_tmp/")

    System.exit(0)
  }
}
