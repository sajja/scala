package com.example.slick.v3.codereview

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain._
import com.example.slick.v3.optimisticlock.OptimisticLockingTest.{BigTable, BigTables, Coffees, UnVersionedAircraftComponent}
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class ServiceRequest()

object CodeReview {
  val prop1 = 0
  val prop2 = 0
  val prop3 = 0
  val prop4 = 0
  val prop5 = 0
  val prop6 = 0

  val bigtables = TableQuery[BigTables]
  val coffees = TableQuery[Coffees]
  val db1 = DatabaseWrapper.db
  val ac = UnVersionedAircraftComponent


  def test() = {
    val x = Seq(1)
    x match {
      case i :: Nil =>
        println("XXXXXXXXX")
      case _ =>
    }
  }

  def doSomeWork1(req: ServiceRequest) = {
    val act1 = coffees.filter(_.name === "111").result
    val aircraftSelectQuery = ac.aircrafts.filter(_.name === "").result

    for {
      aircrafts <- db1.run(aircraftSelectQuery)
      _ <- aircrafts.size match {
        case 1 =>
          val complexCalculatedValue = "value"
          for {
            cofs <- db1.run(coffees.filter(_.name === complexCalculatedValue).result)
            reslut <- db1.run(bigtables.insertOrUpdate(BigTable.apply(prop1,
              prop2, prop3,
              prop4, prop5, prop6,
              7, complexCalculatedValue)))
            _ <- reslut match {
              case 0 => //update
                db1.run(DBIO.successful(Unit))
              case _ =>
                db1.run(DBIO.successful(Unit))
            }
          } yield {
          }
        case _ =>
          db1.run(DBIO.failed(new Exception("Invalid results")))
      }
      expressos <- db1.run(coffees.filter(_.name === "Expresso").result)

    } yield aircrafts
  }

  def doSomeWork() = {
    val act1 = coffees.filter(_.name === "111").result
    val aircraftSelectQuery = ac.aircrafts.filter(_.name === "").result

    for {
      aircrafts <- db1.run(aircraftSelectQuery)
      _ <- aircrafts.size match {
        case 0 =>
          db1.run(ac.aircrafts.filter(_.name === "x-wing").map(_.propulsion).update("iron"))
        case _ =>
          db1.run(DBIO.failed(new Exception("Invalid results")))
      }
    } yield aircrafts
  }

  def main(args: Array[String]): Unit = {
    test()
  }
}
