package com.example.scratchpad


object Test {
  def add(x:Int)=x+42
  def main(args: Array[String]): Unit = {
    val i:Option[Int]  = Some(10)
    val j = i.map(add)
    println(j.get)
  }
}


