package com.example.scratchpad

import java.net.Socket
import java.util.Date

case class Connection()

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  def distribute[A, B](fab: F[(A, B)]): (F[A], F[B]) =
    (map(fab)(_._1), map(fab)(_._2))
}

trait Monad[M[_]] extends Functor[M] {
  def flatMap[A, B](mA: M[A])(f: A => M[B]): M[B]

  def unit[A](a: => A): M[A]

  override def map[A, B](fa: M[A])(f: (A) => B): M[B] = flatMap(fa)(a => unit(f(a)))

  def map2[A, B, C](ma: M[A], mb: M[B])(f: (A, B) => C): M[C] = flatMap(ma)((a: A) => map(mb)((b: B) => f(a, b)))
}

case class Dependency(val i: Int = 10)

class D[A](g: Dependency => A) {
  def apply(c: Dependency) = g(c)

  def flatMap[B](f: A => D[B]): D[B] = {
    new D(c => f(g(c))(c))
  }

  def map[B](f: A => B): D[B] = {
    new D(i => f(g(i)))
  }
}


class Dep[I, A](f: I => A) {
  def apply(i: I): A = f(i)

  def unit[C](a: => C): Dep[I, C] = new Dep[I, C](i => a)

  def flatMap[B](g: A => Dep[I, B]): Dep[I, B] = new Dep[I, B](i => g(f(i))(i))

  def map[B](g: A => B): Dep[I, B] = flatMap(a => unit(g(a)))
}

object Test1 extends App {

  def d1() = (d: Dependency) => s"hello ${d.i}"

  def d2(str: String) = (d: Dependency) => str.length * d.i


  def x1 = new D(d => s"hello ${d.i}")

  def x2(str: String) = new D(d1 => str.length * d1.i)

  def x3 = new D(d => (100, Dependency(d.i + 10)))

  def x4(d1: Dependency) = new D(d => s"hello ${d1.i}")


  val t1 = d1()(Dependency(1))
  val t2 = d2(t1)(Dependency(1))
  println(t2)

  val j = for {
    str <- x1
    p2 <- x2(str)
  } yield p2

  println(j)
  println(j(Dependency(1)))

  val k = for {
    p <- x3
    q <- x4(p._2)
  } yield q

  val l = x3.flatMap((tuple: (Int, Dependency)) => x1.map((s: String) => s"hello ${tuple._2.i}"))

  println(k(Dependency(1)))
  println(l(Dependency(1)))

  def v1 = new Dep[Int, Int](i => i * 100)

  def v2 = new Dep[Int, String](s => "xxx")

  val z = for {
    i <- v1
    j <- v2
  } yield j
}

trait XXX {
  //Date=>String=>String
  type Pref[T] = String=>T
  def df(date: Date)(df: String): String = {
    s"Formated date"
  }

  val i:Date=>Pref[String] = df



}


