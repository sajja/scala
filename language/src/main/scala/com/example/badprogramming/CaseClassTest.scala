package com.example.badprogramming

case class Foo(i: Int)

class Bar(i: Int, j: Int) extends Foo(i)

object BadProgaramming {

  def eqTest() = {
    val foo1 = Foo(10)
    val foo2 = Foo(10)
    println(s"Foo1 == Foo2 , ${foo1 == foo2}")

    val bar1 = new Bar(10, 20)
    val bar2 = new Bar(10, 200)
    println(s"Bar1 == Bar2 , ${bar1 == bar2}")
  }

  def implicitConversion() = {
    implicit def stoi(s: String): Int = Integer.parseInt(s)

    println("100" / 10)
    println("Sajith" / 10)
  }

  def main(args: Array[String]): Unit = {
    //    eqTest()
    implicitConversion()
  }
}