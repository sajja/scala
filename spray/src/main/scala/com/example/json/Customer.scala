package com.example.json

import spray.json.DefaultJsonProtocol
import DefaultJsonProtocol._ // !!! IMPORTANT, else `convertTo` and `toJson` won't work correctly
import spray.json._

case class Color(name: String, red: Int, green: Int, blue: Int)
case class Address(poBox:Int, street:String)
case class Person(name:String,address:Address)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Address)
}

object JsonTest {
  def main(args: Array[String]) {
    implicit val format1 = jsonFormat2(Address)
    implicit val format2 = jsonFormat2(Person)

    //    import MyJsonProtocol._

//    val json = Color("CadetBlue", 95, 158, 160).toJson
//    println(json)
//    val color = json.convertTo[Color]
//    println(color)
    val address = new Address(1,"222")
//    println(address.toJson)
    val cust = new Person("ddd",address)
    println(cust.toJson)
  }
}