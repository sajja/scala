package com.example.patternmatching

class Base
case class Der1 extends Base
case class Der2 extends Base

object TestCase {
  def test(i:Base) {
    i match {
      case i:Der1 => println("Der1")
      case j:Der2 => println("Der2")
      case _ => println("Something else")
    }
  }

  def main(args: Array[String]) {
    test(new Der1)
    test(new Der2)
    test(new Base {

    })
  }
}