package com.example.monad.transformer

import com.example.cake.example1.UserRepositoryComponent

import scalaz.Reader

case class User(name: String)

case class Address(name: String)

trait UserRepo {
  def findUser(name: String): User
}

trait AddressRepo {
  def findAddress(user: User): Address
}

trait T1 {

  object UserRepo {

    import Repositories.ur

    //  def findUser(name: String) = Reader[UserRepo, User](_.findUser(name))
    def findUser(name: String) = ur.map(_.findUser(name))
  }

  object AddressRepo {

    import Repositories.ar

    def findAddress(user: User) = ar.map(_.findAddress(user))

    //  def findAddress(user: User) = Reader[AddressRepo, Address](_.findAddress(user))
  }

  trait Repositories {
    def userRepo: UserRepo

    def addressRepo: AddressRepo
  }

  trait Env {
    def repositories: Repositories
  }

  object ProdUserRepo extends UserRepo {
    override def findUser(name: String): User = {
      User(s"prod $name")
    }
  }

  object ProdAddressRepo extends AddressRepo {
    override def findAddress(user: User): Address = Address(s"prod ${user.name} address")
  }

  object ProdRepos extends Repositories {
    override def userRepo: UserRepo = ProdUserRepo

    override def addressRepo: AddressRepo = ProdAddressRepo
  }

  object prodEnv extends Env {
    override def repositories: Repositories = ProdRepos
  }

  object Repositories {
    val repositories = Reader[Repositories, Repositories](identity)
    val ur = repositories.map(_.userRepo)
    val ar = repositories.map(_.addressRepo)
  }


  object UserService {
    def findUser(name: String): Reader[Repositories, User] = UserRepo.findUser(name)

    def findAddress(userName: String): Reader[Repositories, Address] = for {
      user <- UserRepo.findUser(userName)
      address <- AddressRepo.findAddress(user)
    } yield address
  }

  object AddressService {
    def findAddress(user: User) = Reader[Repositories, Address](r => r.addressRepo.findAddress(user))
  }

}

trait T2 {

  trait UserRepo {
    def findUser(name: String): User
  }

  trait AddressRepo {
    def findAddress(user: User): Address
  }

  trait Repositories {
    def userRepo: UserRepo

    def addressRepo: AddressRepo
  }


  object UserService {
    def findUser(name: String): Reader[Repositories, User] = Reader(_.userRepo.findUser(name))

    def findAddress(userName: String): Reader[Repositories, Address] = Reader {
      r =>
        val user = r.userRepo.findUser(userName)
        r.addressRepo.findAddress(user)
    }
  }


}

object TransformerTest {
  def main(args: Array[String]): Unit = {
    val t1 = new T1 {}
    val us = t1.UserService

    val x = for {
      u <- us.findUser("")
      a <- us.findAddress(u.name)
    } yield a
    x(t1.prodEnv.repositories)
//    x(t1.prod)
  }
}
