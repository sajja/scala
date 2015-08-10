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

  def fun(i: Int) = i

  def foo[A, B](a: A): Try[B] = throw new Exception("oops")

  def main(args: Array[String]) {
    println(exceptionTest1(1))
    println(exceptionTest2(1))
    println(Try(exceptionTest2(0)))
    //    foo(1)

    println(Try("Dd").flatMap(foo))

    val v = for {
      x <- s2(10)
      y<-x.charAt(0)
    } yield y

    println(v)
  }
}
