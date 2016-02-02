package com.example.streams

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.Random

object RandomNumberReactiveStream {

  def make: Stream[Int] = {
    Stream.cons(util.Random.nextInt(10), {
      Thread.sleep(1000)
      make
    })
  }

  val randomNumberSource = Source(make)

  val numberSink = Sink.foreach[Int] {
    i => println(i)
  }

  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem()
    implicit val flowMaterializer = ActorMaterializer()
    randomNumberSource.runWith(numberSink)
  }
}
