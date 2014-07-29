package com.example.allaboutfunctions

import com.example.futures.SlowProcess

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * Created by sajith on 7/27/14.
 */
object FuctionTest1 {
  val fut1 = Future[Int] {
    SlowProcess.thisTakeLongTimeToExecute()
  }

  def addHandlerTheLongWay() {
    fut1.onComplete[Unit](
      (f: Try[Int]) => {
        f match {
          case Success(a) => println("Ok " + f + " Val " + a)
          case Failure(a) => println("Fail " + f + " Val " + a)
        }
      }
    )
  }

  def how_the_fuck_this_works() {
    fut1.onComplete {
          println("xxxxx")
      (f: Try[Int]) => {
        f match {
          case Success(a) =>{
            println("Ok1" + f + " Val " + a)
          }
          case Failure(a) => println("Fail " + f + " Val " + a)
        }
      }
        println("ddd")
      (f: Try[Int]) => {
        f match {
          case Success(a) => {
            println("Ok2" + f + " Val " + a)
          }
          case Failure(a) => println("Fail " + f + " Val " + a)
        }
      }
    }
  }

  def sameHandlerShortWay() {
    fut1.onComplete[Unit] {
      case f@Success(a) => println("Ok " + f + " Val " + a)
      case f@Failure(a) => println("Fail " + f + " Val " + a)
    }
  }

  def main(args: Array[String]) {
    how_the_fuck_this_works()
    Thread.sleep(1000)
  }
}
