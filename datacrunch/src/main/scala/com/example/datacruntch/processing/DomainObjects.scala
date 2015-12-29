package com.example.datacruntch.processing

import java.util.Date

trait DomainObjects {
  type Event <: EventLike
  type AggregatedEvent <: AggregatedEventLike

  trait AggregatedEventLike

  trait EventLike

  class DocumentEvent(val customer: String, val eventType: String, val date: Date, val count: Int, val source: String) extends EventLike {
    def this(customer: String, eventType: String, date: Date, source: String) = this(customer, eventType, date, 1, source)
  }

  class AggregatedDocumentEvent(val id: String, val eventType: String, val source: String, val count: Int) extends AggregatedEventLike
}