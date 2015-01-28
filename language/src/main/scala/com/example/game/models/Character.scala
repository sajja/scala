package com.example.game.models

abstract class Character(name: String, race: Race) {
  var primaryWeapon: Option[Weapon] = None
  //  var secondaryWeapon: Weapon = null

  def fireWeapon(): Int = {
    primaryWeapon match {
      case Some(i) => {
        println(name + " uses " + i.name)
        val damage = i.use()
        println("Attack bonus " + race.attack())
        println("total attach damage " + (damage + race.attack()))
        damage + race.attack()
      }
      case _ => {
        println("How can i fight without weapon")
        -1
      }
    }
  }

  def canEquip(weapon: Weapon): Boolean

  def equipPrimary(weapon: Weapon): Boolean = {
    if (canEquip(weapon)) {
      primaryWeapon = Some(weapon)
      true
    } else {
      false
    }
  }
}

class Ranger(name: String, race: Race) extends Character(name, race) {
  override def canEquip(weapon: Weapon): Boolean = {
    weapon match {
      case i: Bow => true
      case _ => false
    }
  }
}

class Warrior(name: String, race: Race) extends Character(name, race) {
  override def canEquip(weapon: Weapon): Boolean = {
    weapon match {
      case i: Sword => true
      case _ => false
    }
  }
}