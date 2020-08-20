package com.example.mytry

import scala.util.{Failure, Success, Try}

/**
 * Created by sajith on 7/27/14.
 */
object BasicOpWithTry {
  def divide(i: Int, j: Int): Try[Double] = {
    if (j == 0) {
      Failure(new Exception("Div by 0"))
    } else {
      Success(i / j)
    }
  }

  def tryDivide1(i: Int, j: Int) = {
    divide(i, j) match {
      case Success(a) => println(a)
      case Failure(e) => e.printStackTrace()
    }
  }

  def tryDivide2(f: Try[Double]):Unit = {
    f match {
      case Success(a) => println(a)
      case Failure(e) => e.printStackTrace()
    }
  }

  def unitOfWork(f:Try[Double] => Unit) = {
    println("xxxx")
  }

  def anotherUnitOfWork(f:Try[Any] => Unit) {
  }
  def main(args: Array[String]) {
    val x = for {
      i<-divide(1,0)
    } yield i+1
    println("------- " + x.get)
  }
}