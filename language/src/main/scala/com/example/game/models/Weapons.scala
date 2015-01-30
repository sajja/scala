package com.example.game.models

abstract class Weapon(val name: String, val damage: Damage) {
  def use(): Int = {
    println("\tWeapon: " + name)
    val totalDamage = damage.getDamage
    totalDamage
  }

  def canWield(character: Character): Boolean = true
}
class MeeleWeapon(name: String, damage: Damage) extends Weapon(name, damage)
class RangeWeapon(name: String, damage: Damage) extends Weapon(name, damage)
class MagicWeapon(name: String, damage: Damage) extends Weapon(name, damage)

class Bow(name: String, damage: Damage) extends RangeWeapon(name, damage)

class Sword(name: String, damage: Damage) extends MeeleWeapon(name, damage)
class Axe(name: String, damage: Damage) extends MeeleWeapon(name, damage)

class BattleAxe(name: String, damage: Damage) extends Axe(name, damage) {
  override def canWield(character: Character):Boolean = {
    character.race match {
      case i:Orc => true
      case _ => false
    }
  }
}

abstract class Damage(name: String, amount: Int, enhancement: Option[Damage]) {
  def this(name: String, amount: Int) = {
    this(name, amount, None)
  }

  def getDamage: Int = {
    println("\t\tDamage type: " + name + ", Damage: " + amount)
    enhancement match {
      case Some(enhancement) =>  amount + enhancement.getDamage
      case _ => amount
    }
  }
}

case class Normal(name: String, amount: Int) extends Damage(name, amount)
case class FireEnhance(name: String, amount: Int, enhancement: Option[Damage]) extends Damage(name, amount, enhancement)
case class PoisonEnhance(name: String, amount: Int, enhancement: Option[Damage]) extends Damage(name, amount, enhancement)
