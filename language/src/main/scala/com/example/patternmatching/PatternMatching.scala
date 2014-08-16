package com.example.patternmatching

/**
 * Created by sajith on 7/28/14.
 */
object PatternMatching {

  def firstTest(i: Int) {
    i match {
      case 1 => println("i is 1")
      case n:Int if n > 100 => println("i is greater than 100")

      case _ => println("i is not 1")
    }
  }

  def typeTest(i:Any) {
    i match {
      case j:Int => println("Int")
      case j:Long => println("Long")
      case j:String => println("String")
      case _ => println("Other")
    }
  }

  def main(args: Array[String]) {
    firstTest(1)
    firstTest(12)
    firstTest(120)
    typeTest(1)
    typeTest(1L)
    typeTest("")
    typeTest(None)
  }
}
