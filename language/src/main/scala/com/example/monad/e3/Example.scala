package com.example.monad.e3

sealed trait Validation[E, A] {
  def map[B](f: A => B): Validation[E, B]

  def flatMap[B](f: A => Validation[E, B]): Validation[E, B]
}

case class Success[E, A](a: A) extends Validation[E, A] {
  def map[B](f: A => B): Validation[E, B] = new Success(f(a))

  def flatMap[B](f: A => Validation[E, B]): Validation[E, B] = f(a)
}

case class Failure[E, A](e: E) extends Validation[E, A] {
  def map[B](f: A => B): Validation[E, B] = new Failure(e)

  def flatMap[B](f: A => Validation[E, B]): Validation[E, B] = new Failure(e)
}

class Foo {
  def bar: Validation[Exception, Bar] = new Success[Exception, Bar](new Bar)
}

class Bar {
  def baz: Validation[Exception, Baz] = new Success[Exception, Baz](new Baz)
}

class Baz {
  def compute: Validation[Exception, Int] = new Success[Exception, Int](10)
}

case class BarException() extends Exception

case class BazException() extends Exception

case class ComputeException() extends Exception

class Example {
  def main(args: Array[String]) {
    val x = new Foo
    val y = new Success[Exception,Baz](new Baz)
    y.flatMap((baz: Baz) => baz.compute)


  }
}
