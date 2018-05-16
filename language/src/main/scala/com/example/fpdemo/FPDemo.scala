package com.example.fpdemo


trait Maybe[A] {
  def flatMap[B](f: A => Maybe[B]): Maybe[B]

  def map[B](f: A => B): Maybe[B]
}

case class SomeX[A](a: A) extends Maybe[A] {
  def flatMap[B](f: A => Maybe[B]): Maybe[B] = f(a)

  def map[B](f: A => B): Maybe[B] = SomeX[B](f(a))
}

case class NoneX[A]() extends Maybe[A] {
  def flatMap[B](f: A => Maybe[B]): Maybe[B] = NoneX()

  def map[B](f: A => B): Maybe[B] = NoneX()
}


/**
  * f: A---> Maybe[B]
  * g: B ---> Maybe[C]
  *
  * gof A ---> C
  *
  */

object MaybeDemo {
  type B = String
  type C = Int

  //f:A->B
  def f(i: Int): Maybe[B] = {
    if (i == 42) SomeX(s"$i")
    else NoneX()
  }

  //g:B->C
  def g(b: B): Maybe[C] = {
    if (b.equalsIgnoreCase("42")) SomeX(b.toInt)
    else NoneX()
  }


  def main(args: Array[String]): Unit = {
    //    g(f("Hello"))
    println(f(42).flatMap(A => g(A).map(str => s"$str + $A")))
    println(for {
      a <- f(42)
      b <- g(a)
    } yield a + b)
  }
}
