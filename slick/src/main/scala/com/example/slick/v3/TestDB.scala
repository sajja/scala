package com.example.slick.v3

import java.util.UUID

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

object TestDB {
  val db = Database.forConfig("pg")
  val race = TableQuery[Race]
  val bigdata = TableQuery[BigData]

  def createBigDataSchema() = {
    DBIO.seq(
      bigdata.schema.drop,
      bigdata.schema.create
    )
  }

  def createRaceSchema() = {
    DBIO.seq(
      race.schema.drop, //first time this would fail.
      race.schema.create
    )
  }

  def bootstrapRace() = {
    race ++= Seq(
      (1, "Orc", "Strenth"),
      (2, "Human", "Agility"),
      (3, "Undead", "Strenth"),
      (4, "NightElf", "Inteligence"),
      (5, "Orge", "Strenth"),
      (6, "Pladin", "Agility"),
      (7, "Brad", "Inteligence"),
      (8, "Rouge", "Agility")
    )
  }

  def bootstrapBigData(start: Int)(size: Int) = {
    val data = for (i <- start to start+size) yield (i, UUID.randomUUID())
    bigdata ++= data
  }

  def setup() = {
    val bigdataBootstrap = db.run(createBigDataSchema()).flatMap { _ => db.run(bootstrapBigData(0)(500)) }
    val raceBootstap = db.run(createRaceSchema()).flatMap { _ => db.run(bootstrapRace()) }
    Await.result(raceBootstap, 1 seconds)
    Await.result(bigdataBootstrap, 5 seconds)

  }


  def stream(): StreamingDBIO[Seq[(Int, UUID)], (Int, UUID)] = {
    bigdata.result
  }

  def findByType(description: String) = {
    race.filter(_.description === description).result
  }


  def main(args: Array[String]): Unit = {
    setup()
    Await.result(db.run(findByType("Agility")), 10 seconds).foreach(println)

    db.run(findByType("Strenth")).onComplete((triedTuples: Try[Seq[(Int, String, String)]]) => {
      for {
        tup <- triedTuples
      } yield println(tup)
    })

    //do a long process
    println("Work goes on...")


    val x = db.stream(stream().transactionally.withStatementParameters(fetchSize = 100)).foreach {
      (tuple: (Int, UUID)) =>
        println(tuple._1 + " " + tuple._2)
        Thread.sleep(20) //emulating slow sink.
    }

    Await.result(db.run(bootstrapBigData(1003)(200)), 100 seconds)
    println("xxxxxxxxx")


    Await.result(x, 100000 seconds)
  }
}


class Race(tag: Tag) extends Table[(Int, String, String)](tag, "race") {
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey)

  def name: Rep[String] = column[String]("name")

  def description: Rep[String] = column[String]("description")

  override def * : ProvenShape[(Int, String, String)] = (id, name, description)
}

class BigData(tag: Tag) extends Table[(Int, UUID)](tag, "bigdata") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

  def data: Rep[UUID] = column[UUID]("data")

  override def * : ProvenShape[(Int, UUID)] = (id, data)
}

