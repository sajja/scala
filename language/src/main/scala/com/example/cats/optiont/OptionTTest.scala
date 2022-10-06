package com.example.cats.optiont

import cats.data.OptionT

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._

import scala.concurrent.duration.Duration

case class User(name: String)

case class UserPrefs()

case class Recommendation()

object OptionTTest {
  def findUser(id: Int): Future[Option[User]] = Future(Some(User("Sajith")))

  def findUserPrefs(user: User): Future[Option[UserPrefs]] = Future(Some(UserPrefs()))

  def calculateRecomendation(pref: UserPrefs): Option[Recommendation] = ???

  def processReccomendation(rec: Recommendation): Future[Unit] = ???

  def testStandardComposing() = {
    for {
      user <- findUser(1)
      //pref <- findUserPrefs(user) //wont compile
      _ <- user match {
        case Some(u) => findUserPrefs(u)
        case _ => Future(None)
      }
    } yield ()
  }

  def optionTTest() = {
    val res = for {
      user <- OptionT(findUser(1))
      pref <- OptionT(findUserPrefs(user))
      rec <- OptionT.fromOption[Future](calculateRecomendation(pref))
      _ <- OptionT.liftF(processReccomendation(rec))
    } yield (pref)

    println(res.value)
    //    Await.result(res.value,Duration.Inf)
  }

  def main(args: Array[String]): Unit = {
    optionTTest()
  }
}
