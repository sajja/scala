package com.example.reactive


import rx.lang.scala.{Subscriber, Observable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Random

object RemoteSever {
  def data() = {
    var i = Random.nextInt() % 4000
    i = if (i < 0) i * -1 else i

    println(s"Generating next set of values in $i")
    Thread.sleep((i * i) / i)
    (for (i <- 0 to 10) yield i).toIterator
  }
}

object RxTest extends App {
  var i = 0

  def make: Stream[Int] = {
    Stream.cons(i, {
      i = i + 1
      println("Making.....")
      make
    })
  }
  val y = Observable.from(make)
  val z = Observable.from(Seq(2, 3, 4))
//  val x = Observable[String]((subscriber: Subscriber[String]) => )


  z.foreach((i: Int) => {
    println(i)
  })
}
