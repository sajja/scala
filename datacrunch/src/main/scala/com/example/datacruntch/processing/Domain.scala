package com.example.datacruntch.processing

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.Try

trait DomainModelModule {

  type DomainObject <: DomainObjLike

  type Reduced[T <: KeyLike] = (Iterator[Try[(T, Int)]]) => Map[T, Int]

  type Mapped[T <: KeyLike] = String => Try[(T, Int)]

  trait DomainObjLike

  trait KeyLike

  class Key(val date: Date, val customerId: Int, val inputSource: String, val event: String) extends KeyLike {
    override def hashCode = 41 * (41 + date.hashCode()) + customerId + inputSource.hashCode + event.hashCode

    override def equals(other: Any) = other match {
      case that: Key => this.date == that.date && this.customerId == that.customerId && this.inputSource == that.inputSource && this.event == that.event
      case _ => false
    }

    override def toString: String = {
      val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
      s"${df.format(date)} $customerId $event $inputSource"
    }
  }

  case class Event(date: Date, customerId: Int, inputSource: String, event: String, eventCount: Int) extends DomainObjLike

}