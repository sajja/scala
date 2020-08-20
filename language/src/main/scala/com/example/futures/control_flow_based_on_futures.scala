package com.example.futures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Base
class SubType extends Base
class AnotherSubType extends SubType

object FallbackToAnotherFuture {
  val f1 = Future[Double] {
    Thread.sleep(300)
    1/0
  }

  val f2 = Future {
    Thread.sleep(300)
    "Infinity"
  }

  def main(args: Array[String]) {
    f1.fallbackTo(f2).onComplete {
      case e=>
        println("bloody hell... some error")
    }

    Thread.sleep(1000)
  }
}