package com.example.slick.util.domain
import slick.driver.PostgresDriver.api._

/**
 * Created by sajith on 8/22/14.
 */
case class InvoiceEvent(invoiceId: Long, event: String)

class InvoiceEvents(tag: Tag) extends Table[InvoiceEvent](tag, "invoice_doc") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def event = column[String]("event")

  def * = (id, event) <>(InvoiceEvent.tupled, InvoiceEvent.unapply)
}