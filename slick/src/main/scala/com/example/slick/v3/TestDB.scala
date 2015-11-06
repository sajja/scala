package com.example.slick.v3

import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

object TestDB {
  val db = Database.forConfig("pg")
  val race = TableQuery[Race]

  def createSchema() = {
    DBIO.seq(
      race.schema.drop, //first time this would fail.
      race.schema.create
    )
  }

  def bootstrapRecords() = {
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

  def setup() = {
    Await.result(db.run(createSchema()).flatMap {
      _ => db.run(bootstrapRecords())
    }, 10 seconds)
  }

  def stream():StreamingDBIO[Seq[(Int,String,String)], (Int, String,String)]= {
    race.result
  }

  def findByType(description: String) = {
    race.filter(_.description === description).result
  }



  def main(args: Array[String]): Unit = {
    setup()
    Await.result(db.run(findByType("Agility")), 10 seconds).foreach(println)

    db.run(findByType("Strenth")).onComplete((triedTuples: Try[Seq[(Int, String, String)]]) => {
      for{tup<-triedTuples} yield println(tup)
    })

    //do a long process
    println("Work goes on...")

    Await.result(db.run(stream()), 10 seconds).drop(6).foreach(println)
    val g = Await.result(db.run(stream()), 10 seconds)

    Thread.sleep(1000)
  }

}


class Race(tag: Tag) extends Table[(Int, String, String)](tag, "race") {
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey)

  def name: Rep[String] = column[String]("name")

  def description: Rep[String] = column[String]("description")

  override def * : ProvenShape[(Int, String, String)] = (id, name, description)
}

