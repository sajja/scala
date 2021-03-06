package com.example.fpdemo

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait Maybe1[A] {
  def flatMap[B](f: A => Maybe1[B]): Maybe1[B]

  def map[B](f: A => B): Maybe1[B]
}

case class Ok[A](a: A) extends Maybe1[A] {
  override def flatMap[B](f: A => Maybe1[B]): Maybe1[B] = f(a)

  override def map[B](f: A => B): Maybe1[B] = Ok(f(a))
}

case class Err[A]() extends Maybe1[A] {
  override def flatMap[B](f: A => Maybe1[B]): Maybe1[B] = Err[B]()

  override def map[B](f: A => B): Maybe1[B] = Err[B]()
}

object FPDemo2 {

  def f(i: Int): Maybe1[String] = {
    if (i == 42) Ok("Ok")
    else Err()
  }

  def g(str: String): Maybe1[Boolean] = {
    if (str.equalsIgnoreCase("OK")) Ok(true)
    else Err()
  }

  def processValue(i: Int): Option[Int] = Some(i)

  def readValue() = null

  def f1() = Future {
    Thread.sleep(1000)
    println("F1")
  }

  def f2() = Future {
    Thread.sleep(1000)
    println("F2")
  }

  def f3() = Future {
    Thread.sleep(1000)
    println("F3")
  }


  def f4() = {
    val f = f1()
    f.onComplete {
      case _ => println(s"$f F1 one on F4")
    }
    f
  }

  def f5() = {
    val f = for {
      _ <- f2()
      _ <- f4()
    } yield ()
    f.onComplete {
      case _ => println(s"$f F1 one on F5")
    }
    f
  }

  def main(args: Array[String]): Unit = {
    println(f(42).flatMap((str: String) => g(str)))
    println(f(42).flatMap((str: String) => g(str).map((bool: Boolean) => s"$str + $bool")))

    println(
      for {
        p <- f(42)
        a <- f(2)
        b <- g(a)
        c <- f(42)
      } yield {
        s"$a + $b"
      }
    )


    def process() = {
      val f1 = Future("Hello")
      val f2 = Future("Scala")
      val f3 = Future("Future")
      val f4 = Future("Future")


      val result = for {
        r1 <- f1
        r2 <- f2
        r3 <- f3
      } yield ()

      Await.result(result, 2 second)
      result
    }


    Await.result(f5(), 5 second)


  }
}
