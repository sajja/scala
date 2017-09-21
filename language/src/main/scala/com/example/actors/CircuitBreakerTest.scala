package com.example.actors


import scala.concurrent.duration._
import akka.pattern.CircuitBreaker
import akka.pattern.pipe
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class DangerousActor extends Actor with ActorLogging {

  import context.dispatcher

  val breaker =
    new CircuitBreaker(
      context.system.scheduler,
      maxFailures = 5,
      callTimeout = 10.seconds,
      resetTimeout = 1.minute).onOpen(notifyMeOnOpen())

  def notifyMeOnOpen(): Unit =
    log.warning("My CircuitBreaker is now open, and will not close for one minute")

  def dangerousCall: String = "This really isn't that dangerous of a call after all"

  def receive = {
    case "is my middle name" =>
      breaker.withCircuitBreaker(Future(dangerousCall)) pipeTo sender()
    case "block for me" =>
      sender() ! breaker.withSyncCircuitBreaker(dangerousCall)
  }
}

object CircuitBreakerTest {

  import scala.concurrent.ExecutionContext.Implicits.global

  def test1() = {
    val system = ActorSystem("PingPongSystem")

    val evenNumberAsFailure: Try[Int] => Boolean = {
      case Success(n) => n % 2 == 0
      case Failure(_) => true
    }

    val breaker =
      new CircuitBreaker(
        system.scheduler,
        maxFailures = 2,
        callTimeout = 1.seconds,
        resetTimeout = 10 seconds)

    val i = breaker.withCircuitBreaker({
      //      Thread.sleep(3000)
      Future.failed(new Exception("DDDD"))
    })
    i.onComplete((triedInt: Try[Int]) => println(triedInt))


    val j = breaker.withCircuitBreaker({
      //      Thread.sleep(3000)
      Future.failed(new Exception("DDDD"))
    })
    j.onComplete((triedInt: Try[Int]) => println(triedInt))
    val k = breaker.withCircuitBreaker({
      //      Thread.sleep(3000)
      Future.failed(new Exception("DDDD"))
    })
    k.onComplete((triedInt: Try[Int]) => println(triedInt))

    val l = breaker.withCircuitBreaker({
      //      Thread.sleep(3000)
      Future.failed(new Exception("DDDD"))
    })
    l.onComplete((triedInt: Try[Int]) => println(triedInt))
    // this call will return 8888 and increase failure count at the same time
    //    breaker.withCircuitBreaker(Future(8888), evenNumberAsFailure)
  }

  def failFuture() = Future {
    println("calling....")
    Thread.sleep(2000)
    100
  }
  def test2()={
    val system = ActorSystem("PingPongSystem")
    val breaker =
      new CircuitBreaker(
        system.scheduler,
        maxFailures = 2,
        callTimeout = 1.seconds,
        resetTimeout = 10 seconds)


    breaker.withCircuitBreaker(failFuture)
    breaker.withCircuitBreaker(failFuture)
    breaker.withCircuitBreaker(failFuture)
    Thread.sleep(6000)
    println("continue")
    breaker.withCircuitBreaker(failFuture).onComplete((value: Try[Int]) =>println(value))
    breaker.withCircuitBreaker(failFuture).onComplete((value: Try[Int]) =>println(value))

    Thread.sleep(20000)
  }

  def main(args: Array[String]): Unit = {

    test2()
  }
}
