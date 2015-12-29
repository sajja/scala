package com.example.datacruntch.storage.cassandra

import com.datastax.driver.core.Cluster
import com.example.datacruntch.processing.DomainObjects
import com.example.datacruntch.storage.EventStorageModule
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.connectors.{ContactPoint, KeySpaceDef}
import com.websudos.phantom.db.DatabaseImpl
import com.websudos.phantom.dsl._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try


trait CassandraConnection {
  val hosts = "localhost"
  implicit val keySpace: KeySpace = KeySpace("statistics")
  val cluster = Cluster.builder().addContactPoints(hosts).build()
  implicit lazy val session: Session = cluster.connect(keySpace.name)
}

trait CassandraStorageModule extends EventStorageModule with DomainObjects with CassandraConnection {

  override type Event = DocumentEvent

  override type AggregatedEvent = AggregatedDocumentEvent

  override def store(events: Iterable[AggregatedEvent]): Try[Unit] = Try(for (x <- events) store(x))

  class EventCasandraImpl extends CassandraTable[EventCasandraImpl, AggregatedDocumentEvent] {

    object id extends StringColumn(this) with PartitionKey[String]

    object eventType extends StringColumn(this) with PrimaryKey[String]

    object source extends StringColumn(this) with PrimaryKey[String]

    object count extends IntColumn(this)

    override def fromRow(r: Row): AggregatedDocumentEvent = {
      new AggregatedDocumentEvent(id(r), eventType(r), source(r), count(r))
    }
  }

  override def loadAll(): Try[List[AggregatedEvent]] = {
    Try(Await.result(events.select.fetch(), 10 seconds))
  }

  object events extends EventCasandraImpl

  object Defaults {
    val connector = ContactPoint.local.keySpace("statistics")
  }

  object cdb extends db(Defaults.connector)

  class db(val keyspace: KeySpaceDef) extends DatabaseImpl(keyspace) {

    object events extends EventCasandraImpl

  }


  override def store(event: AggregatedEvent): Unit = {
    Await.result(events.insert
      .value(_.id, event.id)
      .value(_.eventType, event.eventType)
      .value(_.source, event.source)
      .value(_.count, event.count).future(), 10 seconds)
  }
}

trait BootstrappableCassandraStorageModule extends CassandraStorageModule {
  def bootstrap(eventList: List[AggregatedDocumentEvent]): Try[Unit] = {
    Try {
      Await.result(cdb.autocreate().future(), 10 seconds)
      Await.result(cdb.autotruncate().future(), 10 seconds)
      eventList.foreach(store)
    }
  }
}


