package com.example.datacruntch.storage

import com.example.datacruntch.processing.DomainObjects

import scala.util.Try

trait EventStorageModule extends DomainObjects {
  def store(event: AggregatedEvent): Unit

  def store(events: Iterable[AggregatedEvent]): Try[Unit]

  def loadAll(): Try[List[AggregatedEvent]]
}
