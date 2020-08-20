package com.example.codereview

case class Foo(i: Int) {}

class Bar(j: Int, k:Int) extends Foo(j)

object BadCoding_NonFinalCaseClasses {

  def main(args: Array[String]): Unit = {
    val f1 = Foo(1)
    val f2 = Foo(1)
    println(f1.equals(f2))

    val b1 = new Bar(1,2)
    val b2 = new Bar(1,3)
    println(b1.equals(b2))//<-------------WRONG
  }
}
