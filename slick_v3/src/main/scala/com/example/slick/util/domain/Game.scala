package com.example.slick.util.domain

import slick.driver.PostgresDriver.api._


case class Player(name: String, id: Int = 0)

case class Ability(name: String, playerId: Int, id: Int = 0)

trait Domain {

  abstract class DOs[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  }

  class Players(tag: Tag) extends DOs[Player](tag, "players") {
    def name = column[String]("name")

    def * = (name, id) <>(Player.tupled, Player.unapply)
  }

  val players = TableQuery[Players]


  class Abilities(tag: Tag) extends DOs[Ability](tag, "ability") {
    def name = column[String]("name")

    def playerId = column[Int]("player_id")

    def * = (name, playerId, id) <>(Ability.tupled, Ability.unapply)

    def player = foreignKey("Player_FK", playerId, players)(_.id)
  }


  val abilities = TableQuery[Abilities]

  val insertAndReturnPKey = players.returning(players.map(_.id))

  def genericInsAndRetKey[A, B <: DOs[A], T <: TableQuery[B]](t: T) = t.returning(t.map(_.id))
}