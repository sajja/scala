package com.example.slick

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain.{Aircraft, Aircrafts}

import scala.slick.driver.PostgresDriver.simple._



object CRUDexamples {
  implicit val aircrafts = TableQuery[Aircrafts]
  implicit val session = DatabaseWrapper.session

  def findById(id: Int) = {
    //    val findId = aircrafts.findBy(_.id)
    //    findId(id).list().headOption
  }

  def createSchema(schema: List[TableQuery[Aircrafts]]) = {
    schema.foreach(println)
  }


  def update(id: Int, query: TableQuery[Aircrafts]) = {
    query.filter(_.id === id).map(s => s.name).update("x")
  }

  def bootstrap() = {
    println(aircrafts.delete + " Record deleted")
    aircrafts += new Aircraft(1, "x wing", "Conventional")
    aircrafts += new Aircraft(2, "y wing", "Conventional")
    aircrafts += new Aircraft(3, "a wing", "Conventional")
    aircrafts += new Aircraft(4, "x-302", "Impulse")
    aircrafts += new Aircraft(5, "x-303", "Hyperdrive")
    aircrafts += new Aircraft(6, "NCC-135", "Warp")
    aircrafts += new Aircraft(7, "Warbird", "Warp")
    aircrafts += new Aircraft(8, "Millenium falcon", "Iron")
    aircrafts += new Aircraft(9, "Death star", "Unknown")
  }


  def selectByPropultion(prop: String)(implicit query: TableQuery[Aircrafts]) = {
    query.filter(_.propulsion === prop)
  }


  def main(args: Array[String]) {
    aircrafts.ddl.create
    bootstrap()
    implicit val j = 10000
    println("\t=========Find by id positive match demo=========")
    //    println(findById(1).get)
    //    println(findById(2).get)
    println("\t=========Find by id negative match demo=========")
    //    println(findById(222).getOrElse(None))
    println("\n\n\t=========Select All=========")
    //    aircrafts.list().foreach(println)
    println("\n\n")
    println("\t=========Filter by propultion Unknown=========")
    selectByPropultion("Unknown").foreach(println)
    println("\n\n")
    println("\t=========Projection: Project propultion=========")
    aircrafts.map(_.propulsion).foreach(println)
    println("\n\n")

    ///not that good way of doing... butt....
    println("\t=========Group by aircracts by propultion=========")
    //    aircrafts.list().map(s => (s.name, s.propulsion)).groupBy((f: ((String, String))) => f._2).map((tuple: (String, List[(String, String)])) => {
    //      (tuple._1, tuple._2.map((tp: (String, String)) => {
    //        tp._1
    //      }))
    //    }).foreach(println)
    //    println("\n\n")

    ///not that good way of doing... butt....
    //same thing in pattern matching.
    println("\t=========Group by aircracts by propultion=========")
    //    aircrafts.list().map(s => (s.name, s.propulsion)).groupBy(_._2).map {
    //      case (name, props) => (name, props.map(_._1))
    //    }.foreach(println)
    //
    println("\n\n")

    //much bettter.
    println("\t=========Group by and count=========")
    val q = aircrafts.groupBy(_.propulsion).map { case (k, v) => (k, v.length) }
    println(q.selectStatement)
    q.foreach(println)
    println("\n\n")

    println("\t=========Insert then read back =========")
    //Inserts
    aircrafts += Aircraft(10, "Battlestar gallactica", "FTL")
    //    println(findById(10).get)

    aircrafts.insert(Aircraft(11, "Cylon Basestar", "Unknown"))
    //    println(findById(11).get)
    println("\n\n")

    println("\t=========Update then read back =========")
    //update
    aircrafts.filter(_.name === "Cylon Basestar").map(_.propulsion).update("FTL")
    //    println(findById(11).get)
    println("\n\n")

    //delete
    println("\t=========Insert Read Delete Read =========")
    aircrafts += Aircraft(12, "Mistake", "FTL")
    //    println(findById(12).get)
    aircrafts.filter(_.id === 12).delete
    println(findById(12))
    println("\n\n")

    //    val l = aircrafts.list().slice(3, 6)


    //    l.foreach(println)
    session.close()
//    val db = Database.forConfig("pg")



  }
}
