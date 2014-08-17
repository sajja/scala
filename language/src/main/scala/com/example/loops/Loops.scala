package com.example.loops

/**
 * Created by sajith on 8/16/14.
 */
object Loops {

  def main(args: Array[String]) {
    val x = List(1,2,3,4,5,6,7,8,9)

    val yieldTest = for {
      i <-x
      if (i > 5)
    } yield i

    println(yieldTest)
  }
}
