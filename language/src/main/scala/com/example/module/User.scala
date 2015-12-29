package com.example.module

/**
  * Created by sajith on 12/16/15.
  */

trait UserModule {
  type User <: UserLike

  trait UserLike {
    def name: String
  }

  def save(user: User): Unit

  def login(user: String, password: String): User
}

trait SimpleUserModule extends UserModule {

  override type User = HardCodedUser

  override def login(user: String, password: String): User = new HardCodedUser(user)

  override def save(user: HardCodedUser): Unit = {

  }

  class HardCodedUser(username: String) extends UserLike {
    override def name: String = s"hard coded user $username"
  }

}

trait SystemModule {
  this: UserModule=>
}

object UserTest {

  def main(args: Array[String]) = {
    val system = new SystemModule with SimpleUserModule
    val user = system.login("x","y")
    system.save(user)
  }
}

