package com.example.exceptionhandling


import scala.util.{Success, Failure, Try}


object Test {

  def s1(i: Int) = {
    if (i == 10) Failure(new Exception("S1 failure"))
    else Success("" + i)
  }

  def s2(i: Int) = {
    if (i == 0) Failure(new Exception("S2 failure"))
    else Success("" + i)
  }

  def exceptionTest1(i: Int) = {
    if (i == 0) throw new Exception("i == 0")
    i
  }

  def exceptionTest2(i: Int): Try[Int] = {
    if (i == 0) throw new Exception("i == 0")
    Try(i)
  }

  def exceptionTest3(i: Int): Try[Int] = {
    if (i == 0) throw new ArithmeticException("i == 0")
    if (i == 1) throw new ArrayIndexOutOfBoundsException("i == 0")
    Try(i)
  }


  def fun(i: Int) = i

  def foo[A, B](a: A): Try[B] = throw new Exception("oops")

  def main(args: Array[String]) {
    println(exceptionTest1(1))
    println(exceptionTest2(1))
    println(Try(exceptionTest2(0)))
    val xxx = Try(exceptionTest3(1)) recover {
      case a:ArithmeticException => Iterator("A")
      case a:ArrayIndexOutOfBoundsException => Iterator("B")
    }
    println(xxx)
    xxx.foreach((value: Object) => {
      println(value)
    })
  }
}
