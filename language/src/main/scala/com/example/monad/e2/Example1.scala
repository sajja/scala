package com.example.monad.e2

trait Option[A] {
   def map[B](f: A => B): Option[B]

   def flatMap[B](f: A => Option[B]): Option[B]

 }

class Some[A](a: A) extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = {
    new Some[B](f(a))
  }


  def get = a

  override def  flatMap[B](f: (A) => Option[B]): Option[B] = f(a)
}

class None[A] extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new None

  override def flatMap[B](f: (A) => Option[B]): Option[B] = new None
}

class Foo {
  def bar: Option[Bar] = new Some[Bar](new Bar)
}

class Bar {
  def baz: Option[Baz] = new Some[Baz](new Baz)
}

class Baz {
  def compute: Int = 42
}

object Example1 {
  def computeBaz(baz: Baz): Int = baz.compute

  def computeBar(bar: Bar): Option[Int] = bar.baz.map {
    computeBaz
  }

  def computeFoo(foo: Foo): Option[Int] = {
    foo.bar.flatMap(computeBar)
  }

  def compute(foo: Option[Foo]): Option[Int] = {
    foo.flatMap(computeFoo)
  }

  def naiveExample() = {
    val x = compute(new Some[Foo](new Foo))
    x match {
      case y: Some[Int] => println(y.get)
    }
  }

  def betterExample() = {
    val x = new Some[Foo](new Foo).flatMap { f => f.bar.flatMap { b => b.baz.map(baz => baz.compute)}}
    x match {
      case y: Some[Int] => println(y.get)
    }
  }

  def bestSolution(): Unit = {
    val x = new Some[Foo](new Foo)

    val res = for {
      f <- x
      b <- f.bar
      baz <- b.baz
    } yield baz.compute


    res match {
      case y: Some[Int] => println(y.get)
    }
  }


  def main(args: Array[String]): Unit = {
    betterExample()
  }
}
