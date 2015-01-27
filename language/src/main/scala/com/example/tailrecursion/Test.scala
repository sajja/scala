package com.example.tailrecursion

/**
 * Created by sajith on 1/27/15.
 */
object Test {

  def factorial(i:Int):Int = {
    if (i <=1) {
      1
    } else {
      i * factorial(i-1)
    }
  }

  def factorial(i:Int, acc:Int):Int = {
    if (i <=1) {
      acc
    } else {
      factorial(i-1, acc * i)
    }
  }

  def factorialNormalRecursive(i:Int) = {
    factorial(i)
  }

  def factorialTailRecursive(i:Int) = {
    factorial(i,1)
  }

  def main(args: Array[String]) {
    println(factorialNormalRecursive(5))
    println(factorialTailRecursive(5))
    println(factorialTailRecursive(50000))//fail in normal recursion
//    println(factorial(5000000)) stack overflow error.

  }
}
