package com.example.game.models

abstract class Weapon(val name: String, val damage:Int) {
  def use(): Int = {
    println("Normal damage : " + damage)
    damage
  }
}

class Bow(name: String, damage:Int) extends Weapon(name, damage) {
}

class Sword(name: String, damage:Int) extends Weapon(name, damage) {
}

class BattleAxe(name: String, damage:Int) extends Weapon(name, damage) {
}