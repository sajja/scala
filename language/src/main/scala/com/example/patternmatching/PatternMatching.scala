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

  def multi(str:String) = {
    val m = Seq("X", "Y", "Z")
    str match {
      case "A" => println("A")
      case "B" | "C" => println("B or C")
      case m => println("xxxx")
      case _ => println("Other")
    }

  }


  def main(args: Array[String]): Unit = {
    val l = List((1,2),(3,4),(5,6))

    val j:Map[String,String] = l.foldLeft(Map[String,String]())((x: Map[String, String], tuple: (Int, Int)) => Map(tuple._1.toString->tuple._2.toString))

    multi("A")
    multi("1A")
    multi("B")
    multi("C")
    multi("X")
  }
}
