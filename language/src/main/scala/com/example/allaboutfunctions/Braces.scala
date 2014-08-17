package com.example.allaboutfunctions

/**
 * Created by sajith on 8/16/14.
 */
object Braces {

  def testFn(i:Int => Int)= {
    println(i(1))
  }


  def main(args: Array[String]) {
    testFn((i:Int)=>i+10)

    //same thing cooler..
    testFn {
      case(i)=>1 + i
    }
  }
}
