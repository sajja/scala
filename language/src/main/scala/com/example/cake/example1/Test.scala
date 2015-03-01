package com.example.cake.example1

sealed case class User(username: String)

trait UserRepoService {
  def userRep(): UserRepo

  trait UserRepo {
    def findById(i: Int): User
  }

}

trait UserRepoServiceEchoImpl extends UserRepoService {
  override def userRep(): UserRepo = new UserRepoImpl
  class UserRepoImpl extends UserRepo {
    override def findById(i: Int): User = {
      println("Echo user repo.....")
      new User(i.toString)
    }
  }
}

trait UserRepoServiceInMemoryImpl extends UserRepoService {
  val rep = Map(1->new User("111"),2->new User("222"))
  override def userRep(): UserRepo = new UserRepoImpl
  class UserRepoImpl extends UserRepo {
    override def findById(i: Int): User = {
      println("Inmemory user repo....")
      rep get i match {
        case i:Some[User] => i.get
        case None=>null
      }
    }
  }
}

trait UserAuthorizationComponent {
  def userAuthorization: UserAuthorization

  trait UserAuthorization {
    def authorize(user: User)
  }

}

// Component implementation
trait UserAuthorizationComponentImpl
  extends UserAuthorizationComponent {
  // Dependencies
  this: UserRepoService =>

  def userAuthorization = new UserAuthorizationImpl

  class UserAuthorizationImpl extends UserAuthorization {
    def authorize(user: User) {
      println("Authorizing " + user.username)
      // Obtaining the dependency and calling a method on it
      userRep.findById(user.username.toInt)
    }
  }

}

object Test {
  def main(args: Array[String]): Unit = {
    val x = Map(1->"1")
    val auth1 = new UserAuthorizationComponentImpl() with UserRepoServiceEchoImpl
    val auth2 = new UserAuthorizationComponentImpl() with UserRepoServiceInMemoryImpl
    auth1.userAuthorization.authorize(new User("1"))
    auth2.userAuthorization.authorize(new User("1"))
  }
}
