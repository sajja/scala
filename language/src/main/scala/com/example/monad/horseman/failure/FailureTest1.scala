package com.example.monad.horseman.failure

import scala.util.Try

abstract class MayBe[+A]() {
  def flatMap[B](f: A => MayBe[B]): MayBe[B]

  def map[B](f: A => B): MayBe[B]
}

case class Err[A]() extends MayBe[A] {
  override def flatMap[B](f: A => MayBe[B]): MayBe[B] = new Err()

  override def map[B](f: (A) => B): MayBe[B] = new Err()
}

case class Ok[A](a: A) extends MayBe[A] {
  override def map[B](f: (A) => B): MayBe[B] = new Ok(f(a))

  override def flatMap[B](f: (A) => MayBe[B]): MayBe[B] = f(a)
}

object Test1 {

  def f(i: Int): Int = i match {
    case 0 => throw new Exception("Zero exception")
    case _ => i * 10
  }

  def g(i: Int): Int = i match {
    case x if x < 0 => throw new Exception("Negative exception")
    case _ => i + 2
  }
}

object Test2 {

  import Test2.{f, g}

  def f(i: Int): MayBe[Int] = i match {
    case 0 => new Err()
    case _ => Ok(i * 10)
  }

  def g(i: Int): MayBe[String] = i match {
    case x if x < 0 => new Err()
    case _ => Ok(s"xx${i * 10}xx")
  }

  def hash(s: String) = s.hashCode
}


object FailureTest1 {

  def test1() = {
    import Test1.{f, g}
    println(g(f(10)))
    println(g(f(11)))
    println(g(f(0)))
    println(g(f(-10)))
    println(g(f(20)))
  }

  def a(i: Int) = Some(i)

  def b(i: Int) = Some(i)


  def x(): Try[Int] = Try {
    ///.....
    100
  }

  def y(): Try[String] = Try {
    ///.....
    "ddd"
  }

  def z(): Try[Boolean] = Try {
    ///.....
    true
  }

  def test2() = {
    for {
      i <- x()
      j <- y()
      k <- z()
    } yield i
  }

  //g . f
  def main(args: Array[String]): Unit = {
    test2()
  }

}
