package com.example.slick.util.domain

import scala.slick.driver.PostgresDriver.simple._

abstract class UnVersionedEntity[T<:UnVersionedEntity[T]](val id: Int, val version: Int = 0) {
  def bumpVersion(): T
}


abstract class VersionedEntity[T<:VersionedEntity[T]](override val id: Int, override val version: Int = 0) extends UnVersionedEntity[T](id) {
  override def bumpVersion(): T
}

case class Aircraft(override val id: Int, name: String, propulsion: String, override val version: Int = 0) extends UnVersionedEntity[Aircraft](id, version) {
  override def bumpVersion() = Aircraft(id, name, propulsion, version + 1)
}

trait Version {
  def id: Rep[Int]

  def version: Rep[Int]
}

abstract class Entity[T](tag: Tag, name: String) extends Table[T](tag, name) with Version {
  def id = column[Int]("id", O.PrimaryKey)

  override def version = column[Int]("version")
}

class Aircrafts(tag: Tag) extends Entity[Aircraft](tag, "aircraft") with Version {

  def name = column[String]("name")

  def propulsion = column[String]("propulsion")


  def * = (id, name, propulsion, version) <> (Aircraft.tupled, Aircraft.unapply _)
}

case class User(id: Int, name: String, email: String, version: Int = 0)

class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Int]("id", O.PrimaryKey)

  def name = column[String]("name")

  def email = column[String]("email")

  def version = column[Int]("version")

  def * = (id, name, email, version) <> (User.tupled, User.unapply _)
}
