package com.example.monad.io

import java.io.{Closeable, IOException}

import scala.io.{BufferedSource, Source}
import scala.util.Try

trait IO[+A] {
  self =>
  def run: A

  def map[B](f: A => B): IO[B] =
    new IO[B] {
      def run = f(self.run)
    }

  def flatMap[B](f: A => IO[B]): IO[B] =
    new IO[B] {
      def run = f(self.run).run
    }
}


trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Monad[M[_]] extends Functor[M] {
  def unit[A](a: => A): M[A]

  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

  def map[A, B](ma: M[A])(f: A => B): M[B] =
    flatMap(ma)(a => unit(f(a)))

  def map2[A, B, C](ma: M[A], mb: M[B])(f: (A, B) => C): M[C] =
    flatMap(ma)(a => map(mb)(b => f(a, b)))
}

object IO extends Monad[IO] {
  def unit[A](a: => A): IO[A] = new IO[A] {
    def run = a
  }

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]) = fa flatMap f

  def apply[A](a: => A): IO[A] = unit(a)
}


object x {}

object Test {
  def ReadLine: IO[String] = IO {
    readLine
  }

  def PrintLine(str:String) = IO {println(str)}


  val v = for {
    x <-ReadLine
    y <-PrintLine(x)
  } yield {}

  v.run



  def op1(i: Iterator[String]): Iterator[String] = {
    i.map(line => s"s$line op1")
  }

  def op2(i: Iterator[String]): Iterator[String] = {
    i.map(line => s"s$line op2")
  }

  def op3(i: Iterator[String]): Map[String, String] = {
    i.foldLeft(Map[String, String]())((x: Map[String, String], s: String) => {
      println("OP3")
      Map("" -> "")
    })
  }

  def main(args: Array[String]): Unit = {
    val reader = IO[Iterator[String]](Source.fromFile("/home/sajith/scratch/scala/language/build.sbt").getLines())
    val upper = reader.flatMap((strings: Iterator[String]) => IO[Iterator[String]] {
      strings.map { case s: String => s.toUpperCase }
    })

    val s = Source.fromFile("/home/sajith/scratch/scala/language/src/main/scala/com/example/monad/io/testFile")

    using[Source,Map[String,String]](s)((source: Source) => {
      val x = op1(s.getLines())
      op3(x)
    })

//    s.getLines()
  }


  def using[R <: Source, T](stream: R)(f: R => T): T =
    try {
      f(stream)
    } finally {
      stream.close()
    }

}
