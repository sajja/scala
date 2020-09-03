package com.example.cats.validation

import cats.data._
import cats.data.Validated._
import cats.implicits._

case class UserDTO(email: String, password: String)

case class Email(value: String)

case class Password(value: String)

case class User(email: Email, password: Password)

class UserValidationException extends Exception("User validation exception")


object ValidationTestOld {
  def isValidEmail(email: String): Boolean = ???

  def isValidPassword(password: String): Boolean = ???


  object User {
    def apply(email: Email, password: Password): User = new User(email, password)

    def fromUserDTO(user: UserDTO): Option[User] = for {
      email <- Email(user.email)
      password <- Password(user.password)
    } yield new User(email, password)
  }

  object Password {
    def apply(password: String): Option[Password] = Some(password).filter(isValidPassword).map(new Password(_))
  }

  object Email {
    def apply(email: String): Option[Email] = Some(email).filter(isValidEmail).map(new Email(_))
  }

  def validateUserVersion0(user: UserDTO): UserDTO =
    if (isValidEmail(user.email) && isValidPassword(user.password)) {
      user
    } else {
      throw new UserValidationException
    }

  def validate1(user: UserDTO): Option[User] = {
    for {
      email <- Email(user.email)
      password <- Password(user.password)
    } yield new User(email, password)
  }

  def validate3(user: UserDTO): Either[String, User] = (
    Email(user.email).toRight("Email error"),
    Password(user.password).toRight("Password error")
  ) match {
    case (Right(email), Right(password)) => Right(User(email, password))
    case (Left(error), Right(_)) => Left(error)
    case (Right(_), Left(error)) => Left(error)
    case (Left(e1), Left(e2)) => Left(e1 ++ e2)
  }


  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}

  import cats.implicits._

  type ValidationResult[A] = ValidatedNec[String, A]

  case class DemoUser(name: String)

  def validateUserName(userName: DemoUser): ValidationResult[DemoUser] = {
    (if (userName.name == "sajith")
      userName.validNec
    else
      s"Invalid: ${userName.name}".invalidNec)
  }

  def validate4(userName: DemoUser): ValidatedNec[String, DemoUser] = {
    (if (userName.name == "sajith")
      userName.validNec
    else
      s"Invalid: ${userName.name}".invalidNec)
  }


  def main(args: Array[String]): Unit = {
    val i = validateUserName(DemoUser("sajith"))
    val j = validateUserName(DemoUser("sajith"))
    val k = validateUserName(DemoUser("sajith"))
    val z = (i, j, k).mapN((user: DemoUser, user1: DemoUser, u: DemoUser) => (user, user1))
    z match {
      case Valid(_) => println("Valid:::::")
      case Invalid(s) => println(s)
    }


  }

}
