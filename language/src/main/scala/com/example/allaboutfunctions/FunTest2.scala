package com.example.allaboutfunctions

/**
 * Created by sajith on 7/27/14.
 */
object FunTest2 {
  def f1(i:Int) = f2(i)
  def f2(i:Int) = println("F2")

  def main(args: Array[String]) {
    f1(1)
  }
}
