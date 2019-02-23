package com.example.slick

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain._
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._


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


  abstract class AbstarctRepo[T <: UnVersionedEntity[T], X <: Entity[T]] {
    def eR: TableQuery[X]

    def update1(ent: T) = {
      eR.filter((e: Entity[T]) => e.id === ent.id).update(ent)
    }
  }

  abstract class AbstarctVersionedRepo[T <: VersionedEntity[T], X <: Entity[T]] extends AbstarctRepo[T, X] {
    def eR: TableQuery[X]

    override def update1(ent: T) = {
      eR.filter((e: Entity[T]) => e.id === ent.id && e.version === ent.version).update(ent.bumpVersion())
    }
  }

  object UnVersionedAircraftComponent extends AbstarctRepo[Aircraft, Aircrafts] {
    val aircrafts = TableQuery[Aircrafts]
    val eR = TableQuery[Aircrafts]

    def withVersion() = {
    }
  }

  object VersionedAircraftComponent extends AbstarctVersionedRepo[VersionedAircraft, VersionedAircrafts] {
    val aircrafts = TableQuery[VersionedAircrafts]
    val eR = TableQuery[VersionedAircrafts]

    def withVersion() = {
    }
  }

  def bootstrap() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      UnVersionedAircraftComponent.aircrafts.schema.drop,
      UnVersionedAircraftComponent.aircrafts.schema.create,
      UnVersionedAircraftComponent.aircrafts += Aircraft(100, "x-wing", "iron"),
      UnVersionedAircraftComponent.aircrafts += Aircraft(101, "y-wing", "Antimatter")
    )

    Await.result(db.run(setup), 10 seconds)
  }

  def bootstrap1() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      (suppliers.schema ++ coffees.schema).drop,
      (suppliers.schema ++ coffees.schema).create,

      // Insert some suppliers
      suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
      suppliers += (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
      suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
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
  }

  def insert(): Unit = {
    val db = DatabaseWrapper.db
    val ins = DBIO.seq(
      coffees += ("Test", 101, 10.0, 0, 0, 0)
    )

    Await.result(db.run(ins), 10 seconds)
  }

  def find(id: Int): Aircraft = {
    val db = DatabaseWrapper.db
    Await.result(db.run(UnVersionedAircraftComponent.aircrafts.filter(_.id === id).result), 10 seconds).head
  }


  def findVersioned(id: Int): VersionedAircraft = {
    val db = DatabaseWrapper.db
    Await.result(db.run(VersionedAircraftComponent.aircrafts.filter(_.id === id).result), 10 seconds).head
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

  def update2(ac: Aircraft) = {
    val db = DatabaseWrapper.db
    val q = UnVersionedAircraftComponent.aircrafts.filter(a => a.id === ac.id)
    val yyy = Await.result(db.run(q.result), 10 seconds)
    val q2 = q.filter(e => e.version === ac.version)
    val z = q2.update(ac.bumpVersion())
    Await.result(db.run(z), 10 seconds)
  }


  def main(args: Array[String]) {
    val db = DatabaseWrapper.db
    bootstrap()
    listAll()
    var ac4 = find(101)

    //    println(find(101))
    val i1 = UnVersionedAircraftComponent.update1(ac4.copy(name = ac4.name + "1"))
    Await.result(db.run(i1), 10 seconds)
    val i2 = UnVersionedAircraftComponent.update1(ac4.copy(name = ac4.name + "2"))
    Await.result(db.run(i2), 10 seconds)
    println(find(101))

    var ac5 = findVersioned(101)
    val i3 = VersionedAircraftComponent.update1(ac5.copy(name = ac5.name + "___111"))
    Await.result(db.run(i3), 10 seconds)
    val i4 = VersionedAircraftComponent.update1(ac5.copy(name = ac5.name + "___222"))
    Await.result(db.run(i4), 10 seconds)
    println(find(101))
  }
}

