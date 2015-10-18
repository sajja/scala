package com.example.cake.example1

case class User(username: String, password: String)

trait UserRepositoryComponent {
  val userRepository: UserRepository

  class UserRepository {
    def authenticate(u: String, p: String): User = {
      val user = new User(u, p)
      println("authenticating user: " + user)
      user
    }

    def create(user: User) = println("creating user: " + user)

    def delete(user: User) = println("deleting user: " + user)
  }

}

trait UserServiceComponent {
  this: UserRepositoryComponent =>
  val userService = new UserService

  class UserService {
    def authenticate(username: String, password: String): User =
      userRepository.authenticate(username, password)

    def create(username: String, password: String) =
      userRepository.create(new User(username, password))

    def delete(user: User) = userRepository.delete(user)
  }

}


object ComponentRegistry extends
UserServiceComponent with
UserRepositoryComponent {
  override val userRepository: ComponentRegistry.UserRepository = new UserRepository
}

