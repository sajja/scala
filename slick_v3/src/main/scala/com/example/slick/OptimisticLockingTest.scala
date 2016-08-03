package com.example.slick

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain.{User, Users, Aircrafts, Aircraft}
import slick.lifted.TableQuery
import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object OptimisticLockingTest {
  val coffees = TableQuery[Coffees]

  class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey)

    // This is the primary key column
    def name = column[String]("SUP_NAME")

    def street = column[String]("STREET")

    def city = column[String]("CITY")

    def state = column[String]("STATE")

    def zip = column[String]("ZIP")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip)
  }

  val suppliers = TableQuery[Suppliers]

  class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int, Int)](tag, "COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)

    def supID = column[Int]("SUP_ID")

    def price = column[Double]("PRICE")

    def sales = column[Int]("SALES")

    def total = column[Int]("TOTAL")

    def version = column[Int]("VERSION")

    def * = (name, supID, price, sales, total, version)

    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id)
  }

  val aircrafts = TableQuery[Aircrafts]

  def bootstrap() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      aircrafts.schema.drop,
      aircrafts.schema.create,
      aircrafts += Aircraft(100, "x-wing", "iron"),
      aircrafts += Aircraft(101, "y-wing", "Antimatter")
    )

    Await.result(db.run(setup), 10 seconds)
  }

  def bootstrap1() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      (suppliers.schema ++ coffees.schema).drop,
      (suppliers.schema ++ coffees.schema).create,

      // Insert some suppliers
      suppliers +=(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
      suppliers +=(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
      suppliers +=(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
      coffees ++= Seq(
        ("Colombian", 101, 7.99, 0, 0, 0),
        ("French_Roast", 49, 8.99, 0, 0, 0),
        ("Espresso", 150, 9.99, 0, 0, 0),
        ("Colombian_Decaf", 101, 8.99, 0, 0, 0),
        ("xxxx", 49, 9.99, 0, 0, 0)
      )
    )

    val setupFuture = db.run(setup)
    Await.result(setupFuture, 10 second)
  }

  def listAll(): Unit = {
    val db = DatabaseWrapper.db
    //    println(Await.result(db.run(coffees.map(coffee => (coffee.name, coffee.price, coffee.version)).result), 10 second))
    println(Await.result(db.run(aircrafts.result), 10 second))
  }

  def insert(): Unit = {
    val db = DatabaseWrapper.db
    val ins = DBIO.seq(
      coffees +=("Test", 101, 10.0, 0, 0, 0)
    )

    Await.result(db.run(ins), 10 seconds)
  }

  def find(id: Int): Aircraft = {
    val db = DatabaseWrapper.db
    Await.result(db.run(aircrafts.filter(_.id === id).result), 10 seconds).head
  }

  def update(cof: String): Unit = {
    val db1 = DatabaseWrapper.db
    val db2 = DatabaseWrapper.db
    val up1 = coffees.filter(_.name === cof).map(_.price).update(101)
    val up2 = coffees.filter(_.name === cof).map(_.price).update(201)
    Await.result(db1.run(up1), 10 seconds)
    Await.result(db2.run(up2), 10 seconds)
  }

  def update(amount: Double, query: Query[Rep[Double], Double, Seq]) = {
    val db = DatabaseWrapper.db
    val i: Int = Await.result(db.run(query.update(amount)), 10 seconds)
    println(s"records $i udpated")
  }

  def update(ac: Aircraft) = {
    val db = DatabaseWrapper.db
    val i = Await.result(db.run(aircrafts.filter(a => a.id === ac.id && a.version === ac.version).update(ac.bumpVersion())), 10 seconds)
    if (i != 0) println("Updated")
    else println("Stale object")
  }

  def main(args: Array[String]) {
    bootstrap()
    //    insert()
    listAll()
    val ac1 = find(101)//0
    val ac2 = find(101)//0
    update(Aircraft(ac1.id, ac1.name, "Experimental1"))//->1
    update(Aircraft(ac1.id, ac1.name, "Experimental2"))//0
    println(find(101))


    //    val q1 = find("Test")
    //    val q2 = find("Test")
    //
    //    update(1000, q1)
    //    listAll()
    //    update(2000, q1)
    //    listAll()
    //    println("xxx")
    //    update(new Coffees("Test"))
  }
}
