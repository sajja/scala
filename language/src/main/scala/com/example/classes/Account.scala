package com.example.classes

/**
 * Created by sajith on 7/28/14.
 */
case class PersonName(first: String, last: String, middle: Option[String])

object PersonName {
  def apply(first: String, last: String): PersonName = {
    new PersonName(first, last, None)
  }
}

object Test {
  val me = PersonName("Jessica", "Kerr")
//  val you = PersonName("Jessica", "Kerr", Option("ddd"))

  //  val greeting = me match {
  //    case PersonName(first, last) => s"Hello, $first $last"
  //  }

  def main(args: Array[String]) {
    //    println(greeting)
    println(PersonName.unapply(me))
  }
}