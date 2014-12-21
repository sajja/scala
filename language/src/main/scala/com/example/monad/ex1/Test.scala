package com.example.monad.ex1

trait Option[A] {
  def map[B](f: A => B): Option[B]

  def flatMap[B](f: A => Option[B]): Option[B]
}


class Some[A](a: A) extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new Some[B](f(a))

  override def flatMap[B](f: (A) => Option[B]): Option[B] = f(a)

  def get(): A = a
}

class None[A] extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = new None[B]

  override def flatMap[B](f: (A) => Option[B]): Option[B] = new None[B]

}


class Foo(a: Int) {
  def bar: Option[Bar] = {
    if (a == 42)
      new Some[Bar](new Bar(a))
    else
      new None
  }
}

class Bar(i:Int) {
  def baz: Option[Baz] = new Some[Baz](new Baz(i))
}

class Baz(i:Int) {
  def compute: Int = 42
}


object Test {
  def compute(foo: Option[Foo]) = {
    val v = foo.flatMap(
      (foo: Foo) => foo.bar.flatMap(
        (bar: Bar) => bar.baz.map(
          (baz: Baz) => baz.compute
        )
      )
    )

    val g = for {
      f <- foo
      b <- f.bar
      bz <- b.baz
    } yield bz.compute

    v match {
      case a: Some[Int] => println(a.get())
      case a: None[Int] => println("no op")
    }
  }

  def main(args: Array[String]) {
    compute(new Some(new Foo(421)))
    val v = new Some()
  }
}
