package com.example.futures


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}

/**
 * Created by sajith on 7/24/14.
 */
object SlowProcess {
  def thisTakeLongTimeToExecute(): Int = {
    Thread.sleep(3000)
    1000
  }
}


object BlockingCall {
  def main(args: Array[String]) {
    println("Staring to call slow method")
    val abc = SlowProcess.thisTakeLongTimeToExecute()
    println("Done calling slow method")
  }
}

object UnBlockingCall {
  def main(args: Array[String]) {
    println("Staring to call slow method")
    val abc:Future[Int] = Future[Int] {
      SlowProcess.thisTakeLongTimeToExecute()
    }

    abc.onComplete {
      case Success(a) => println("This is called later " + a)
      case Failure(a) => println("Failed")
    }

    println("Done calling slow method")
    Thread.sleep(19000)
  }
}