package com.example.monad.experiment

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global


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

object TestBasic {
  def findUser(name: String): Future[Option[User]] = Future(Some(User(name)))


  case class User(name: String)

  case class Address(name: String)

  def findAddress(userName: String): Future[Option[Address]] = Future(Some(Address(s"$userName's Address")))

  def main(args: Array[String]): Unit = {
    val z:Future[Option[Address]] = (for {
      a <- FutureO(findUser("xxx"))
      b <- FutureO(findAddress(a.name))
    } yield b).future

    println(z)
  }
}
