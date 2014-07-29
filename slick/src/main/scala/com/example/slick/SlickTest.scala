package com.example.slick

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain.{Aircraft, Aircrafts}

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.AbstractTable

/**
 * Created by sajith on 7/20/14.
 */

object SlickTest {
  implicit val aircraft = TableQuery[Aircrafts]
  implicit val session = DatabaseWrapper.session


  def createSchema(schema:List[TableQuery[Aircrafts]]) = {
//    schema.foreach((_) =m{println(_)})
  }
  def showQuery(implicit query:TableQuery[Aircrafts]) = {
    println(query.selectStatement)
  }

  def select(implicit query:TableQuery[Aircrafts]) = {
    val output = query.run
    output.foreach(println)
  }

  def insert(id:Int, name: String, propulsion:String, query:TableQuery[Aircrafts]) = {
    query += Aircraft(id,name,propulsion)
  }

  def update(id:Int, query:TableQuery[Aircrafts]) = {
    query.filter(_.id === id).map(s=> s.name).update("x")
  }


  def main(args: Array[String]) {

//      createSchema(List(aircraft))
//    showQuery(aircraft)
    select
//    insert(12,"Puddle jumper", "Warp drive", aircraft)
    update(11,aircraft)
    select
    session.close()
  }
}
