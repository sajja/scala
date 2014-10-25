package com.example.map_flatmap

/**
 * Created by sajith on 10/16/14.
 */
object MapFlatmap {
  def print(x:List[Int]) = {
    0::x
  }

  def main(args: Array[String]) {
    val x = List(List(1,2), List(2,3), List(4,5))
    println(x)
    println(x.map(print))
    println(x.flatMap(print))
  }
}
