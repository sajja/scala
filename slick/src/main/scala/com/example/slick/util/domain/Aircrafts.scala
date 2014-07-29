package com.example.slick.util.domain

import scala.slick.driver.PostgresDriver.simple._
case class Aircraft(id: Int, name:String, propulsion:String)

class Aircrafts(tag: Tag) extends Table[Aircraft](tag, "aircraft") {
  def id = column[Int]("id")
  def name = column[String]("name")
  def propulsion = column[String]("propulsion")
  def * = (id,name,propulsion)<> (Aircraft.tupled, Aircraft.unapply _)
}
