package com.example.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinRouter

/**
 * Problem we try to solve is sum of squares from 0-n
 */


class BadSolution {
  def squre(n: Int) = n * n

  def calculate(lower: Int, upper: Int, acc: Int): Int = {
    if (lower > upper) {
      acc
    } else {
      calculate(lower + 1, upper, acc + squre(lower))
    }
  }
}

object BadSolutionTest {
  def main(args: Array[String]) {
    //0,1,2,3
    //0+1+4+9=14
    val start = System.nanoTime()
    val res = new BadSolution().calculate(0, 100, 0)
    val end = System.nanoTime()
    println("Result " + res + " Time " + (end - start))
  }
}


case class StartCalculation(upper: Int)

case object StopCalculation

case class Calculate(i: Int, upper: Int)

case class Result(j:Int, i: Int, upper: Int)


class Worker extends Actor {
  def squre(n: Int) = n * n

  override def receive: Actor.Receive = {
    case Calculate(value, upper) => {
      val sq = squre(value)
      sender ! Result(value, sq, upper)
    }
  }
}

class Manager(workers: Int) extends Actor {
  var responseCount: Int = 0
  var accRes: Long = 0
  val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers)), name = "workerRouter")

  override def receive: Actor.Receive = {
    case Result(j,value, upper) => {
      responseCount += 1
      accRes += value
      if (responseCount == upper) {
        println("result " + accRes)
        context.system.shutdown()
      }
    }
    case StartCalculation(upper) => {
      for (i <-1 to upper) workerRouter ! Calculate(i,upper)
    }
  }

}

object BetterSolutionTest {
  def main(args: Array[String]) {
    println(new BadSolution().calculate(0,999,0))
    val system = ActorSystem("A")
    val master = system.actorOf(Props(new Manager(4)), name = "manager")
    master ! StartCalculation(999)

  }
}