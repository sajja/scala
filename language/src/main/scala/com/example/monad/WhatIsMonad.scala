package com.example.monad

import scala.concurrent.Future

object WhatIsMonad {
  type Exception = String;

  trait M[A]

  case class Raise[A](e: Exception) extends M[A]

  case class Return[A](a: A) extends M[A]

  class Term()

  case class Con(a: Int) extends Term

  case class Div(t: Term, u: Term) extends Term


  def pure[A](a: A): M[A] = Return(a)

  def bind[A, B](m: M[A], k: A => M[B]): M[B] = {
    m match {
      case Raise(e) => Raise(e)
      case Return(a) => k(a)
    }
  }
}

object AnotherTest {


  type M[A] = Maybe[A]

  //  def f[A, B](a: A): M[B]
  //
  //  def g[B, C](b: B): M[C]

  //  val z = bind[String, Int, Int](f[Int, String](100), g)

  //  def bind[A, B, C](m: M[A], k: B => M[C]): M[C]

  //  g[String, Int](f[Int, String](100))


  abstract class Maybe[A] {
    def bind[B](f: A => Maybe[B]): Maybe[B]
  }

  case class Failure[A]() extends Maybe[A] {
    override def bind[B](z: A => Maybe[B]): Maybe[B] = Failure()
  }

  case class Success[A](a: A) extends Maybe[A] {
    override def bind[B](z: A => Maybe[B]): Maybe[B] = z(a)
  }


  case class User(id: Int)

  case class Recommendation()

}

object TestRun {
  def processValue(i: Int): Int = ???

  def readValue(): Option[Int] = ???

  def main(args: Array[String]): Unit = {
    import com.example.monad.AnotherTest._
    def loadUser(id: Int): Maybe[User] = id match {
      case 1 | 2 => Success(User(id))
      case _ => Failure()
    }

    def calculateRecommendation(user: User): Maybe[Recommendation] = user.id match {
      case 1 => Success(Recommendation())
      case _ => Failure()
    }

    val z = loadUser(1).bind((user: User) => {
      calculateRecommendation(user)
    });
    println(z)

    def processInt(i: Int): Future[Int] = ???

    def processString(i: String): Future[Int] = ???

    def processBoolean(i: Boolean): Future[Int] = ???

    val f1 = processInt(1);
    val f2 = processString("ddd")
    val f3 = processBoolean(true)
    //wont compile
    /*
    for {
      v1 <- f1
      v2 <- f2
      v3 <- f3
    } yield {}
  */
  }
}

//object TestWhatIsMonad {
//
//  import WhatIsMonad._
//
//  def eval(term: Term): M[Int] = {
//    term match {
//      case Con(a) => pure(a)
//      case Div(t, u) =>
//        bind(eval(t), (a: Int) =>
//          bind(eval(u), (b: Int) =>
//            if (b == 0)
//              Raise("div by0")
//            else
//              Pure(a / b)
//          ))
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//
//
//    val z = eval(Div(1, 4))
//
//  }
//}
