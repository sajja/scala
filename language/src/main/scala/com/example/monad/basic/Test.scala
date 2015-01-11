package com.example.monad.basic

trait Option[A] {
  def map[B](f: A => B): Option[B]

  def flatMap[B](f: A => Option[B]): Option[B]
}

class Some[A](val a: A) extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new Some[B](f(a))

  override def flatMap[B](f: (A) => Option[B]): Option[B] = f(a)
}

class None[A] extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new None

  override def flatMap[B](f: (A) => Option[B]): Option[B] = new None
}

class Foo(i: Int) {
  def bar(): Option[Bar] = {
    if (i == 10) {
      new None
    } else {
      new Some[Bar](new Bar(i))
    }
  }
}

class Bar(i: Int) {
  def baz(): Option[Baz] = {
    if (i == 20) {
      new None
    } else {
      new Some[Baz](new Baz(i))
    }
  }
}

class Baz(i: Int) {
  def compute() = {
    1
  }
}

object Test {


  def computeBaz(baz: Baz): Int = {
    baz.compute()
  }

  def computeBar(bar: Bar): Option[Int] = {
    bar.baz().map(computeBaz)
  }

  def computeFoo(foo: Foo): Option[Int] = {
    foo.bar().flatMap(computeBar)
  }

  def compute(foo: Option[Foo]): Option[Int] = {
    foo.flatMap(computeFoo)
  }

  def main(args: Array[String]) {
    val baz = new Baz(100)
    println(baz.compute())

    val bar = new Bar(100)
    val r2 = bar.baz().map((baz: Baz) => baz.compute())

    val foo = new Foo(100)
    val r3 = foo.bar().flatMap((bar: Bar) => bar.baz().map((baz: Baz) => baz.compute()))

    val someOkFoo = new Some[Foo](foo)
    val r4 = someOkFoo.flatMap((foo: Foo) => foo.bar().flatMap((bar: Bar) => bar.baz().map((baz: Baz) => baz.compute())))
    val r5 = someOkFoo.flatMap {
      foo => foo.bar.flatMap {
        bar => bar.baz.map {
          baz => baz.compute()
        }
      }
    }


    println("OK operation ------------------")
    r5 match {
      case a:Some[Int] => println(a.a)
      case a:None[_] => println("No Op")

    }


    val someErrorFoo1 = new Some[Foo](new Foo(20))
    println("Not ok operation ------------------")
    val r6 = someErrorFoo1.flatMap {
      foo => foo.bar.flatMap {
        bar => bar.baz.map {
          baz => baz.compute()
        }
      }
    }

    r6 match {
      case a:Some[Int] => println(a.a)
      case a:None[_] => println("No Op")

    }

    //compact
    val g = for {
      f <- someErrorFoo1
      b <- f.bar
      bz <- b.baz
    } yield bz.compute

    println("")
  }
}
