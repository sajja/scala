package com.example.cats.validation

import cats.data._
import cats.data.Validated._
import cats.implicits._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class UserDTO(email: String, password: String)

case class Email(value: String)

case class Password(value: String)

case class User(email: Email, password: Password)

class UserValidationException extends Exception("User validation exception")


object ValidationTestOld {

  import cats.data.Validated.{Invalid, Valid}

  import cats.implicits._

  type ValidationResult[A] = ValidatedNec[Error, A]

  import scala.concurrent.ExecutionContext.Implicits.global

  def getUser(user: Option[String]) = user

  def getPassword(pw: Option[String]) = pw

  def getSalary(sal: Option[Int]) = sal

  def getAge(age: Option[Int]) = age

  def dbOpNone(i: Int): Future[Option[Int]] = Future {
    None
  }

  def dbOp1(i: Int): Future[Option[Int]] = Future {
    println("dbOpt1")
    Some(i)
  }

  def dbOp2(j: Int): Future[Option[Int]] = Future {
    println("dbOpt2")
    Some(j)
  }


  def dbOp3(j: Int): Future[Option[Int]] = Future {
    println("dbOpt3")
    Some(j)
  }


  def dbOp4(j: Int): Future[Option[Int]] = Future {
    println("dbOpt4")
    Some(j)
  }

  def ggg() = {
    println("gggg")
  }

  case class Error(s: String)

  def validateOption[T](v: Option[T], err: String): ValidationResult[T] = {
    if (v.isDefined) v.get.validNec
    else Error(s"Invalid $err").invalidNec
  }

  //classic nested option for validation
  def classicValidation() = {
    getUser(Some("xavior")) match {
      case Some(user) =>
        getPassword(Some("1234")) match {
          case Some(pass) =>
            getSalary(Some(12)) match {
              case Some(s) =>
                getAge(Some(12)) match {
                  case Some(age) =>
                  case None =>
                    println("Invalid age")
                }
              case None =>
                println("Invalid sal")
            }
          case None =>
            println("Invalid pass")
        }
      case None =>
        println("Invalid user")
    }
  }

  def lift[T](value: Option[T], err: String): Future[T] = {
    if (value.isDefined) Future.successful(value.get)
    else Future.failed(new Exception(err))
  }


  def classicFutureOptionNest() = {
    //How doyou fix this ... optOpt1 is a Option[T]
    //    for {
    //      optOp1 <- dbOp1(1)
    //      //      optOp2 <- dbOp2(Some(optOp1)) //Wont even compile
    //    } yield {}

    //
    //    //Because of this people write really bad code.
    //    dbOp1(1).flatMap {
    //      case Some(a) =>
    //        //with A
    //        //      write some code which wash your windows
    //        //      tie your shoes with a
    //        //      Comb your hair
    //        dbOp2(a).flatMap {
    //          case Some(b) =>
    //            // with b,
    //            //        Change the gravitation constant of the universe
    //            //        Disrupt spacetime continuum
    //            //
    //            dbOp3(b).flatMap {
    //              case Some(c) =>
    //                //with C
    //                //        Charge your distruptors
    //                //        Arm your phasers
    //                //        Load your torpedoes
    //                //        All hands battle stations
    //                dbOp4(c).flatMap {
    //                  case Some(d) =>
    //                    // With D
    //                    //        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tempus dolor vitae lectus consequat, vitae congue nulla fringilla.
    //                    //        Curabitur facilisis porttitor sem. Curabitur tempor ligula neque, vel molestie mauris interdum semper.
    //                    //        Vestibulum ut quam cursus, tempus risus vel, pretium magna. In mollis tristique risus, tempor
    //                    //        lobortis justo interdum eget. Quisque ut eros id elit sagittis commodo a ut ex. Cras lacinia
    //                    //        sagittis sapien vel imperdiet. Aliquam ac commodo diam, quis condimentum eros. Mauris ut
    //                    //        euismod lectus. Pellentesque ut consequat orci. Vestibulum et odio eros. Nullam vel justo vitae ante congue
    //                    //        posuere. Maecenas lectus lacus, iaculis vitae posuere et, semper et dui.
    //                    Future {}
    //                  case None =>
    //                    // Handle your errors
    //                    // Make some apologies
    //                    // Prepare for late night dev-ops
    //                    Future {}
    //                }
    //            }
    //          case None =>
    //            // Handle your errors
    //            // Make some apologies
    //            // Prepare for late night dev-ops
    //            Future {}
    //        }
    //      case None => {
    //        // Handle your errors
    //        // Make some apologies
    //        // Prepare for late night dev-ops
    //        Future {
    //        }
    //      }
    //    }
    //
    //    val xxx = for {
    //      optOp1 <- dbOp1(1)
    //      x <- lift(optOp1, "Invalid option optOpt1")
    //      _ <- {
    //        //with A
    //        //      write some code which wash your windows
    //        //      tie your shoes with a
    //        //      Comb your hair
    //        Future {}
    //      }
    //      y <- dbOp2(x)
    //      z <- lift(y, "Invalid option y")
    //      // you see where the pattern....
    //      // Now we eliminate the netsting of case statements.
    //    } yield {}


    //this works, however no error handling if the option is None
    val yyyy = for {
      //      a <- OptionT(dbOp1(1))
      a <- OptionT(dbOp1(1))
      _ <- OptionT(dbOpNone(1))
      b <- {
        //with A
        //      write some code which wash your windows
        //      tie your shoes with a
        //      Comb your hair
        OptionT(dbOp2(a))
      }

    } yield {}

    Await.result(yyyy.value, Duration.Inf)

    //may be we lift the Option to another Future
  }


  //classic nested option for validation
  def nestedOptionsWithCats() = {
    val valU = validateOption(getUser(None), "Error user")
    val valPass = validateOption(getUser(Some("11")), "Error Pass")
    val valSal = validateOption(getSalary(Some(11)), "Error Sal")
    val valAge = validateOption(getAge(None), "Error Age")
    (valU, valPass, valSal, valAge).mapN((i, j, k, l) => {}) match {
      case Valid(_) => println("All valid")
      case Invalid(s) => {
        s.iterator.foreach(println)
        println(s"Validation failure\n$s")
      }
    }
  }

  def main(args: Array[String]): Unit = {
    //    formValidationTestWithCats()
    //    nestedOptionsWithCats()
    classicFutureOptionNest()
  }
}
