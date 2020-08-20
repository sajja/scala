package com.example.monad.reader

import scalaz.Reader

trait Domain {

  case class User(name: String)

  case class Address(name: String)

}

trait ReaderSimple extends Domain {
  def userRepo = new UserRepo()

  class UserRepo {
    def findUser(name: String) = User(name)

    def findAddress(user: User) = Address(user.name)
  }

  object UserService {
    def findUser(name: String): Reader[UserRepo, User] = Reader(_.findUser(name))

    def findAddress(user: User): Reader[UserRepo, Address] = Reader(_.findAddress(user))
  }

}

/**
  * Running multiple methods on same configuration/dependency.
  */
object ReaderSimpleTest {
  def main(args: Array[String]) {
    val rs = new ReaderSimple {}
    val x = rs.UserService.findUser("Sajith")
    println(x.run(rs.userRepo))

    println(
      (for {
        user <- rs.UserService.findUser("sajith")
        add <- rs.UserService.findAddress(user)
      } yield add).run(rs.userRepo)
    )
  }
}

/**
  * Now we have two dependencies. User service require UserRepo to access user and AddressRepo to access Address
  */
trait ReaderComplex1 extends Domain {

  def prodConfig = new Config {
    override def userRepo(): UserRepo = new UserRepo {
      override def findUser(name: String): User = User(s"Prod $name")
    }

    override def addRepo(): AddressRepo = new AddressRepo {
      override def findAddress(user: User): Address = Address(s"${user.name}")
    }
  }

  trait Config {
    def userRepo(): UserRepo

    def addRepo(): AddressRepo
  }


  trait UserRepo {
    def findUser(name: String): User
  }

  trait AddressRepo {
    def findAddress(user: User): Address
  }

  object UserService {
    def findUser(name: String): Reader[Config, User] = Reader(_.userRepo().findUser(name))

    def findAddress(user: User): Reader[Config, Address] = Reader(_.addRepo().findAddress(user))
  }
}

object ReaderComplex1Test {
  def main(args: Array[String]) {
    val rs = new ReaderComplex1 {}
    println(
      (for {
        user <- rs.UserService.findUser("name")
        add <- rs.UserService.findAddress(user)
      } yield add).run(rs.prodConfig)
    )
  }
}