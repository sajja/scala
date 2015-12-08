package com.example.datacruntch.storage.cassandra

import java.util.{Calendar, Date}

import akka.event.slf4j.SLF4JLogging
import com.datastax.driver.core.Cluster
import com.example.datacruntch.storage.EventStorageModule
import com.websudos.phantom.column.DateColumn
import com.websudos.phantom.connectors.{ContactPoint, KeySpaceDef}
import com.websudos.phantom.db.DatabaseImpl
import com.websudos.phantom.dsl._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

trait CassandraEventStorageModule extends EventStorageModule with CassandraConnection with SLF4JLogging {

  override type DomainObject = Event

  override def store(listOf: List[Event]): Try[Unit] = {
    Try(listOf.foreach(store))
  }

  def bootstrap(events: List[Event]): Try[Unit] = {
    Try(new Exception("Cannot bootstrap"))
  }

  override def loadByDate(date: Date): List[Event] = {
    Await.result(events.select.where(_.date eqs date).fetch(), 10 seconds)
  }

  override def store(e: Event): Unit = {
    Await.result(events.insert
      .value(_.date, e.date)
      .value(_.customerId, e.customerId)
      .value(_.event, e.event)
      .value(_.inputSource, e.inputSource)
      .value(_.eventCount, e.eventCount).future(), 10 seconds)
  }

  def load(from: Date, to: Date) = {
    val keys = getPartitionKeysInBetween(from, to)
    Await.result(events.select.fetch(), 10 seconds)
  }


  def getPartitionKeysInBetween(from: Date, to: Date) = {
    val cal = Calendar.getInstance()
    cal.setTime(from)
    var start = cal.getTime
    var keys = List[Date]()

    while (start.before(to)) {
      keys = start :: keys
      cal.add(Calendar.HOUR, 1)
      start = cal.getTime
    }

    keys
  }

  object events extends EventCasandraImpl

  class EventCasandraImpl extends CassandraTable[EventCasandraImpl, Event] {

    object date extends DateColumn(this) with PartitionKey[Date]

    object customerId extends IntColumn(this) with PrimaryKey[Int]

    object inputSource extends StringColumn(this) with PrimaryKey[String]

    object event extends StringColumn(this) with PrimaryKey[String]

    object eventCount extends IntColumn(this)

    override def fromRow(r: Row): Event = {
      Event(date(r), customerId(r), inputSource(r), event(r), eventCount(r))
    }
  }

}

trait BootstrapedCassandraEventStorageModule extends CassandraEventStorageModule {

  override def bootstrap(eventList: List[Event]): Try[Unit] = {
    Try {
      Await.result(cdb.autocreate().future(), 10 seconds)
      Await.result(cdb.autotruncate().future(), 10 seconds)
      eventList.foreach(store)
    }
  }

  def loadAll(): List[Event] = Await.result(events.select.fetch(), 10 seconds)

  def countAll(): Long = Await.result(events.select.count().fetch(), 10 seconds).sum

  object Defaults {
    val connector = ContactPoint.local.keySpace("demo")
  }

  object cdb extends db(Defaults.connector)

  class db(val keyspace: KeySpaceDef) extends DatabaseImpl(keyspace) {

    object events extends EventCasandraImpl

  }

}

trait CassandraConnection {
  val hosts = "localhost"
  implicit val keySpace: KeySpace = KeySpace("demo")
  val cluster = Cluster.builder().addContactPoints(hosts).build()
  implicit lazy val session: Session = cluster.connect(keySpace.name)
}

