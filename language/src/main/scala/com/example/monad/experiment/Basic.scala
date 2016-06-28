package com.example.monad.experiment

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}


case class FutureO[+A](future: Future[Option[A]]) extends AnyVal {
  def flatMap[B](f: A => FutureO[B])(implicit ec: ExecutionContext): FutureO[B] = {
    val newFuture = future.flatMap {
      case Some(a) => f(a).future
      case None => Future.successful(None)
    }
    FutureO(newFuture)
  }

  def map[B](f: A => B)(implicit ec: ExecutionContext): FutureO[B] = {
    FutureO(future.map(option => option map f))
  }
}

case class TryOption[+A](t: Try[Option[A]]) extends AnyVal {

  def flatMap[B](f: A => TryOption[B]): TryOption[B] = {
    val z = t.flatMap {
      case Some(a) => f(a).t
      case None => Failure(new Exception(""))
    }
    TryOption(z)
  }

  def map[B](f: A => B): TryOption[B] = {
    TryOption(t.map(op => op.map(f)))
  }
}

object TestBasic {

  case class User(name: String)

  case class Address(name: String)

  def findAddress(userName: String): Future[Option[Address]] = Future(Some(Address(s"$userName's Address")))

  def findUser(name: String): Future[Option[User]] = Future(Some(User(name)))

  def fU(p: String): Try[Option[User]] = Try(Some(User(p)))

  def fA(p: User): Try[Option[Address]] = Try(Some(Address(p.name)))


  def main(args: Array[String]): Unit = {
    val z: Future[Option[Address]] = (for {
      a <- FutureO(findUser("xxx"))
      b <- FutureO(findAddress(a.name))
    } yield b).future

    println(z)

    val y = for {
      u <- TryOption(fU("x"))
      a <- TryOption(fA(u))
    } yield a
  }
}
