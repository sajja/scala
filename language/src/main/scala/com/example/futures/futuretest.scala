package com.example.futures


import java.lang.Exception

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success, Try}

/**
  * Created by sajith on 7/24/14.
  */
object SlowProcess {
  def thisTakeLongTimeToExecute(): Int = {
    println("Slow method is starting")
    Thread.sleep(30)
    println("slow method completes execution ...with result 1000")
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
  def fn(f: Try[Int]) = println("vvvv")

  def main(args: Array[String]) {
    println("Staring to call slow method")
    val abc: Future[Int] = Future[Int] {
      SlowProcess.thisTakeLongTimeToExecute()
    }

    abc.onComplete {
      case Success(a) => println("This is called later " + a)
      case Failure(a) => println("Failed")
    }

    Thread.sleep(1000)
  }
}

object AnotherTest {
  def doWork(i: Int): Future[Double] = {
    Future[Double] {
      100 / i
    }
  }

  def main(args: Array[String]) {
    //    val  x:Future[Double] = doWork(10)
    val x: Future[Double] = doWork(0)
    x onFailure {
      case e: Exception => println("Are you kidding....")
    }
    x onSuccess {
      case v: Double => println("value " + v)
    }

    Thread.sleep(1000)
  }
}


object MultipleDependentFutures {

  def calculateScore(name: String, score: Int): Future[Int] = {
    Future[Int] {
      Thread.sleep(100)
      score
    }
  }

  def main(args: Array[String]) {
    val mS = calculateScore("ME", 100)
    val yS = calculateScore("U", 1000)

    val didIwin = for {
      myScore <- mS
      yourScore <- yS
    } yield myScore > yourScore

    didIwin onSuccess {
      case b: Boolean => println("I won " + b)
    }

    Thread.sleep(1000)
  }
}


object AnotherTest1 {
  def test1(i: Int) = Future {
    Thread.sleep(i)
    println("Test1 called")
    throw new Exception("test 1 exception")
  }.recoverWith {
    case _ => Future {
      throw new Exception("test w exception")
    }
  }


  def main(args: Array[String]) {
    println("-------------------------------------")
    val i = 0
    val x1 = Future {
      if (i == 0) {
        println("FUture failed ... ")
        throw new Exception("XXXXXXXXXX")
      } else 10000000
    }

    val x2 = x1.recoverWith {
      case e: Exception => Future {
        1000
      }
    }

    val x3 = x1.recover {
      case e: Exception => {
        1000
      }
    }

    //
    //    x1.recoverWith{
    //      case x:Exception => Future{println("Recovered. ....................")}
    //    }

    println(Await.result(x3, Duration.Inf))
    println("-------------------------------------")
    println("All done...")

    val x = Seq(1, 2, 3)
    val y = x.map(i => Future {
      if (i == 2) throw new Exception("XXX")
    })
    val z = Future.sequence(y)
    val a = Await.result(z,Duration.Inf)




    Thread.sleep(10000)


  }
}