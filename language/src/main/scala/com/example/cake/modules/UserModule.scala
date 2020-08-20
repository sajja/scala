package com.example.cake.modules

trait UserModule {
  def login(name: String, password: String): Option[User]

  def save(user: User)

  case class User(name: String, password: String)

}

trait HardCodedUserModule extends UserModule {
  val users = List(new User("sajith", "silva"), new User("a", "b"))

  override def login(name: String, password: String): Option[User] = users.find(u => u.name == name && u.password == password)

  override def save(user: User): Unit = user :: users
}

trait MessageModule extends UserModule {
  def save(m: Message)

  case class Message()

}

object Test2 {
  def main(args: Array[String]) {
    val messageModule = new MessageModule with HardCodedUserModule {
      override def save(m: Message): Unit = {}
    }

    println(messageModule.login("sajith", "silva"))
  }
}
