package com.example.game.models

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorSystem, Actor}

abstract class Character(val name: String, var hitPoints: Int, val race: Race)(implicit system:ActorSystem) extends Actor {
  var primaryWeapon: Option[Weapon] = None

  val actor = system.actorOf(Props(this),name)

  override def receive: Receive = ???

  def useWeapon(target: Character): Int = {
    primaryWeapon match {
      case Some(i) => {
        println(name + " attacks " + target.name)
        val damage = i.use()
        val totalDamage = damage + race.attack()
        println("\n\tBase damage: " + totalDamage)
        println("\tAttack bonus: " + race.attack())
        println("\tTotal damage: " + totalDamage)
//        target.takeDamage(damage)
        totalDamage
      }
      case _ => {
        println("How can i fight without weapon")
        -1
      }
    }
  }

  def takeDamage(damage: Int) = hitPoints -= damage

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

class Ranger(name: String, hitPoints: Int, race: Race) (implicit system:ActorSystem) extends Character(name, hitPoints, race) {
  override def canEquip(weapon: Weapon): Boolean = {
    weapon match {
      case i: Bow => i.canWield(this)
      case _ => false
    }
  }
}

class Warrior(name: String, hitPoints: Int, race: Race) (implicit system:ActorSystem) extends Character(name, hitPoints, race) {
  override def canEquip(weapon: Weapon): Boolean = {
    weapon match {
      case w: MeeleWeapon => w.canWield(this)
      case _ => false
    }
  }
}