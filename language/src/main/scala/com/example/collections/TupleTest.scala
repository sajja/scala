package com.example.collections

object TupleTest {

  def printTup(a:(Any,Any)) = {
    println(a._1)
    println(a._2)
  }

  def matchTup(tup:(Any,Any)) = {
    tup match {
      case ("Answer", a) => println("Answer to ultimate question of life universe and everything is " + a)
      case (1,2) => println("1=>2")
      case ("hello",_) => println("Hello to you too")
      case(_)=>println("Any match")
    }

  }
  def tupleTest() = {
    val testTup = ("Answer", 42)

    printTup(testTup)
    matchTup(testTup)

    val anotherTup = 1->2
    printTup(anotherTup)
    matchTup(anotherTup)

    matchTup("resistance is"->"futile")
    matchTup("hello" -> "a")
  }

  def main(args: Array[String]) {
    tupleTest()
  }
}
