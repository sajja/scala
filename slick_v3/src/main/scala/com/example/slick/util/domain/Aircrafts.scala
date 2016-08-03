package com.example.slick.util.domain

import scala.slick.driver.PostgresDriver.simple._

case class Aircraft(id: Int, name: String, propulsion: String, version: Int = 0) {
  def bumpVersion() = Aircraft(id, name, propulsion, version + 1)
}

class Aircrafts(tag: Tag) extends Table[Aircraft](tag, "aircraft") {
  def id = column[Int]("id", O.PrimaryKey)

  def name = column[String]("name")

  def propulsion = column[String]("propulsion")

  def version = column[Int]("version")

  def * = (id, name, propulsion, version) <>(Aircraft.tupled, Aircraft.unapply _)
}

case class User(id: Int, name: String, email: String, version: Int = 0)

class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Int]("id", O.PrimaryKey)

  def name = column[String]("name")

  def email = column[String]("email")

  def version = column[Int]("version")

  def * = (id, name, email, version) <>(User.tupled, User.unapply _)
}
