package com.example.exceptionhandling

import com.example.monad.validation.Failure

import scala.util.Try


object Test {

  def exceptionTest1(i:Int) = {
    if(i == 0) throw new Exception("i == 0")
    i
  }

  def exceptionTest2(i:Int):Try[Int] = {
    if(i == 0) throw new Exception("i == 0")
    Try(i)
  }

  def foo[A, B](a: A): Try[B] = throw new Exception("oops")

  def main(args: Array[String]) {
    println(exceptionTest1(1))
    println(exceptionTest2(1))
    println(Try(exceptionTest2(0)))
//    foo(1)

    println(Try("Dd").flatMap(foo))
  }


}
