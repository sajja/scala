package com.example.byname

object Test {
  def byVal(f:Int) = {
    println("Executing f()")
  }

  def byName(f: => Int) = {
    println("Executing f()")
    f//we dont care about the function arguments.
  }

  def somefn() = {
    println("Inside some fn()")
    10
  }


  def somefn1(i:Int) = {
    println("Inside some fn1()")
    i
  }
  def main(args: Array[String]): Unit = {
    byVal(somefn())
    println("\n")
    byName(somefn())
    println("\n")
    byName(somefn1(10))
  }
}
