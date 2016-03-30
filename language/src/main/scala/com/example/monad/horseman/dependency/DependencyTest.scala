package com.example.monad.horseman.dependency

//type P = B=>C
//A=>B=>C
//A => P(x)

trait DependencyTest1 {

  case class User(i: Int)

  type Config = String

  class Storage {
    def load(i: Int) = {
      val config = new Config("Test")
      println(s"Loading id $i with $config")
      User(i)
    }
  }

  class UserModule {
    val storage = new Storage()

    def loadUser(i: Int) = storage.load(i)
  }

  class System {
    val user = new UserModule()

    def login(id: Int) = user.loadUser(id)
  }

}

trait DependencyTest2 {
  type Config = String

  case class User(i: Int)

  class Storage {
    def load(i: Int, c: Config) = {
      println(s"Loading id $i with $c")
      User(i)
    }
  }

  class UserModule {
    val storage = new Storage()

    def loadUser(i: Int, c: Config) = storage.load(i, c)
  }

  class System {
    val user = new UserModule()

    def login(id: Int) = user.loadUser(id, new Config)
  }

}


trait DependencyTest3 {
  type Config = String
  type Dependency[T] = Config => T

  case class User(i: Int)

  class Storage {

    //Int => Config => User
    //Int => Dependency[User]
    // A => P(B)
    def load(i: Int): Dependency[User] = (c: Config) => {
      println(s"Loading id $i with $c")
      User(i)
    }
  }

  class UserModule {
    val storage = new Storage()

    def loadUser(i: Int) = storage.load(i)
  }

  class System {
    val user = new UserModule()

    def login(id: Int) = user.loadUser(id)(new Config)
  }

}

trait DependencyTest4 {
  type Config = String
  type Dependency[T] = Config => T

  case class User(i: Int)

  case class Address(name: String)

  class Storage {

    //Int => Config => User
    //Int => Dependency[User]
    // A => P(B)
    def loadUser(i: Int): Dependency[User] = (c: Config) => {
      println(s"Loading id $i with $c")
      User(i)
    }

    //User=>Dependency[Address]
    def loadAddress(user: User) = (c: Config) => new Address(s"${user.i}'s address")
  }

  class UserModule {
    val storage = new Storage()

    //    def loadUser(i: Int) = storage.load(i)
  }

  class System {
    val user = new UserModule()

    //    def login(id: Int) = user.loadUser(id)(new Config)
  }

}


trait DependencyTest5 {
  type Config = String
  type Dependency[T] = Config => T

  class Dep[A](f: Config => A) {
    def apply(d: Config) = f(d)

    def flatMap[B](g: A => Dep[B]): Dep[B] = new Dep[B](d => g(f(d))(d))

    def map[B](g: A => B): Dep[B] = new Dep[B](d => g(f(d)))
  }

  case class User(i: Int)

  case class Address(name: String)

  def system = new System()

  class Storage {
    //Int => Config => User
    //Int => Dependency[User]
    // A => P(B)
    def loadUser(i: Int): Dep[User] = {
      new Dep((f: Config) => new User(i))
    }

    //User=>Dependency[Address]
    def loadAddress(user: User) = new Dep((f) => {
      println(s"loading address with $f")
      new Address(s"user ${user.i} addresss")
    })
  }

  class UserModule {
    val storage = new Storage()

    def loadUser(i: Int) = storage.loadUser(i)
  }

  class System {

    def doWork() {
      val user = new UserModule()
      val u = user.storage.loadUser(100)
      user.storage.loadUser(100).flatMap(u => user.storage.loadAddress(u))(new Config("test"))
    }
  }
}

object DependencyTest extends App {
  val uit = new DependencyTest5 {}
  uit.system.doWork()
}
