package com.example.game.models

abstract class Weapon(val name: String) {
  def fire(): Unit = println("using the " + name)
}

class Bow(name: String) extends Weapon(name) {
}

class Sword(name: String) extends Weapon(name) {
}