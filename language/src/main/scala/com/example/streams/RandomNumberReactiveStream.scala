package com.example.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait MyObservable[T] {
  def subscribe(observer: MyObserver[T]): Subscription
}

trait MyObserver[T] {
  def onNext(t: T): Unit

  def onError(e: Throwable): Unit

  def onComplete(): Unit
}

trait Subscription {
  def unsubscribe(): Unit
}

class RandomNumberPublisher extends MyObservable[Int] {
  var x: MyObserver[Int] = null


  val system = ActorSystem("cw")
  val ticker = system.scheduler.schedule(1 second, 3 second, new Runnable {
    override def run(): Unit = {
      val i = util.Random.nextInt(10)
      println(s"generated random number $i")
      x.onNext(i)
    }
  })

  override def subscribe(observer: MyObserver[Int]): Subscription = {
    x = observer
    new Subscription {
      override def unsubscribe(): Unit = ticker.cancel()
    }
  }
}

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


object ObserverTest {
  def main(args: Array[String]) {
    val rnp = new RandomNumberPublisher()
    val subscription = rnp.subscribe(new MyObserver[Int] {
      override def onError(e: Throwable): Unit = ???

      override def onComplete(): Unit = ???

      override def onNext(t: Int): Unit = {
        println(s"Observer got $t")
      }
    })
    Thread.sleep(6000)
    println("Cancelling...")
    subscription.unsubscribe()
  }
}


object RandomNumberReactiveStream1 {

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

