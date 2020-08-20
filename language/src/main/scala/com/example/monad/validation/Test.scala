package com.example.monad.validation

trait Validation[E, A] {
  def map[B](f: A => B): Validation[E, B]

  def flatMap[B](f: A => Validation[E, B]): Validation[E, B]

  def liftFail[F](f: E => F): Validation[F, A]
}

case class Success[E, A](a: A) extends Validation[E, A] {
  override def map[B](f: (A) => B): Validation[E, B] = new Success[E, B](f(a))

  override def flatMap[B](f: (A) => Validation[E, B]): Validation[E, B] = f(a)

  override def liftFail[F](f: E => F): Validation[F, A] = new Success[F, A](a)
}

case class Failure[E, A](e: E) extends Validation[E, A] {
  override def map[B](f: (A) => B): Validation[E, B] = new Failure[E, B](e)

  override def flatMap[B](f: (A) => Validation[E, B]): Validation[E, B] = new Failure[E, B](e)

  override def liftFail[F](f: E => F): Validation[F, A] = new Failure[F, A](f(e))
}

case class FooException(mag: String) extends Exception(mag)

case class BarException(mag: String) extends Exception(mag)

case class BazException(mag: String) extends Exception(mag)

class Foo(i: Int) {
  def bar(): Validation[FooException, Bar] = {
    if (i == 10) {
      new Failure[FooException, Bar](new FooException("Error in foo"))
    } else {
      new Success[FooException, Bar](new Bar(i))
    }
  }
}

class Bar(i: Int) {
  def baz(): Validation[BarException, Baz] = {
    if (i == 20) {
      new Failure[BarException, Baz](new BarException("Failure in bar"))
    } else {
      new Success[BarException, Baz](new Baz(i))
    }
  }
}

class Baz(i: Int) {
  def compute(): Validation[BazException, Int] = {
    if (i == 42) {
      new Success[BazException, Int](42)
    } else {
      new Failure[BazException, Int](new BazException("it should get 42"))
    }
  }
}

object Test {

  def compute(baz: Baz): Validation[BazException, Int] = {
    baz.compute()
  }

  def compute(bar: Bar): Validation[BarException, Int] = {
    bar.baz().flatMap {
      baz => compute(baz).liftFail((exception: BazException) => new BarException(""))
    }
  }

  def main(args: Array[String]): Unit = {
    val foo = new Foo(100)
    val bar = new Bar(100)
    val baz = new Baz(100)

    val res = for {
      bar <- foo.bar()
      baz <- bar.baz.liftFail((exception: BarException) => new FooException(""))
    } yield baz.compute()

    compute(baz)
    bar.baz().liftFail((exception: BarException) => new BazException(exception.mag)).flatMap((baz: Baz) => new Success[BazException, Int](1) {})
    bar.baz().liftFail((exception: BarException) => new BazException(exception.mag)).flatMap((baz: Baz) => compute(baz))

    foo.bar().liftFail { e => new BazException(e.mag)}.flatMap {
      bar => bar.baz().liftFail { e => new BazException(e.mag)}.flatMap {
        baz => baz.compute()
      }
    }

    for {
      bar <- foo.bar().liftFail { e => new BazException(e.mag)}
      baz <- bar.baz().liftFail { e => new BazException(e.mag)}
    } yield baz.compute()

    //    foo.bar().flatMap((bar: Bar) => )
    println("")

  }


}