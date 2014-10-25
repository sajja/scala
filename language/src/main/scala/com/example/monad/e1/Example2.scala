package com.example.monad.e1

import java.io.IOException


trait Option[A] {
  def map[B](f: A => B): Option[B]

  def flatMap[B](f: A => Option[B]): Option[B]
}

trait Validation[E, A] {
  def map[B](f: A => B): Validation[E, B]

  def flatMap[B](f: A => Validation[E, B]): Validation[E, B]
}

class Success[E, A](a: A) extends Validation[E, A] {
  override def map[B](f: (A) => B): Validation[E, B] = new Success(f(a))

  override def flatMap[B](f: (A) => Validation[E, B]): Validation[E, B] = f(a)
}

class Error[E, A] extends Validation[E, A] {
  override def map[B](f: (A) => B): Validation[E, B] = new Error

  override def flatMap[B](f: (A) => Validation[E, B]): Validation[E,B] = new Error
}


class Some[A](a: A) extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new Some[B](f(a))

  override def flatMap[B](f: (A) => Option[B]): Option[B] = f(a)

  def get = a
}

class None[A] extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new None[B]

  override def flatMap[B](f: (A) => Option[B]): Option[B] = new None[B]
}

class A {
  def b: Some[B] = new Some[B](new B)
}

class B {
  def c: Some[C] = new Some[C](new C)
}

class C {
  def comp(): Int = 42
}

class P {
  def doP(i: Int): Option[Q] = {
    if (i == 0) {
      return new None
    }
    new Some[Q](new Q)
  }
}

class Q {
  def doQ(i: Int): Option[R] = {
    if (i == 0) {
      return new None
    }

    new Some[R](new R)
  }
}

class R {
  def doR(i: Int) = {
    42
  }
}


class TCPLayer() {
  def openSocket(i: Int): Validation[IOException,String] = {
    if (i == 0) {
      throw new IOException()
    }
    new Success[IOException,String]("Hurraaa")
  }
}


object Example2 {
  def example1() = {
    val x = new Some[A](new A)
    val res = x.flatMap(a => a.b.flatMap(b => b.c).map(c => c.comp()))
    println("--------Example 1-------")
    printRes(res)

    val anotherRes = for {
      a <- x
      b <- a.b
      c <- b.c
    } yield c.comp()

    printRes(anotherRes)
  }


  def example2() = {
    val x = new Some[P](new P)
    println("--------Example 2-------")
    printRes(x.flatMap(p => p.doP(1).flatMap(q => q.doQ(1)).map(r => r.doR(10))))
    printRes(x.flatMap(p => p.doP(1).flatMap(q => q.doQ(0)).map(r => r.doR(10))))
    printRes {
      for {
        p <- x
        q <- p.doP(10)
        r <- q.doQ(10)
      } yield r.doR(100)
    }
  }

  def printRes(x: Option[_]): Unit = {
    x match {
      case i: Some[_] => println(i.get)
      case _ => println("computation failure")
    }
  }

  def main(args: Array[String]) {
    example1()
    example2()
  }
}