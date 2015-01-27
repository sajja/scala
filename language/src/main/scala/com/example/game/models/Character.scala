package com.example.game.models

abstract class Character(name: String, race: Race) {
  var primaryWeapon: Option[Weapon] = None
  //  var secondaryWeapon: Weapon = null

  def fireWeapon(): Boolean = {
    primaryWeapon match {
      case Some(i) => {
        i.fire()
        true
      }
      case _ => {
        println("How can i fight without weapon")
        false
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

class Rouge(name: String, race: Race) extends Character(name, race) {
  override def canEquip(weapon: Weapon): Boolean = true
}

