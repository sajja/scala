package com.example.functioncomposition

/**
  * Created by sajith on 2/28/15.
  */
object Test {

  def a(i: Int): String = s"$i"

  def b(s: String): Double = s.toDouble

  def c(i:Int):Double = b(a(i))


  val sq = (i: Int) => i * i
  val inc = (i: Int) => i + 1
  val doub = (i: Int) => i * 2

  def main(args: Array[String]) {
    val inc_and_double = doub compose inc
    val squre_and_inc = inc compose sq
    val double_and_inc = doub andThen inc

    println(inc_and_double(10))
    println(squre_and_inc(2))
    println(double_and_inc(10))
  }

}
