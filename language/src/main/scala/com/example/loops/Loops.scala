package com.example.loops

object Loops {

  def ifTest() = {
    val id = 1001
    val isHundread  = if (id == 100) {
      "Hundread"
    } else {
      "somthign elese"
    }

    println(isHundread)
  }

  def main(args: Array[String]) {
    val x = List(1,2,3,4,5,6,7,8,9)

    val yieldTest = for {
      i <-x
      if (i > 5)
    } yield i

    println(yieldTest)
    ifTest()
    val x1 = for (i<-x) i
    println(x1)
  }
}
