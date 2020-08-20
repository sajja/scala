package com.example.state.world

import scala.util.Random

class State(val currentHitPoints: Int, val baseHitPoints: Int)

case class Healthy(current: Int, base: Int) extends State(current, base)

case class Dead(base: Int) extends State(0, base)

case class Blodied(current: Int, base: Int) extends State(current, base)

object Event {
  def apply(f: State => State)(current: State) = {
    f(current)
  }
}

object Events {
  def posInt: Int = {
    val i = Random.nextInt(10)
    if (i > 0) i
    else posInt
  }

  type event = State => State

  def apply(et: String): event = {
    et match {
      case "damage" => (f: State) => f match {
        case state: Dead =>
          println("You cannot damage dead unit")
          state
        case state: Healthy =>
          val hp = posInt
          println("Dealing some damage " + hp)
          val newHp = state.currentHitPoints - hp
          if (newHp <= 0) new Dead(state.base)
          else if (newHp * 2 < state.currentHitPoints) Blodied(newHp, state.base)
          else new Healthy(newHp, state.base)
      }
      case "heal" => (f: State) => f match {
        case state: Dead =>
          println("You cannot heal a dead unit")
          state
        case state: Healthy =>
          val hp = posInt
          println("Healing some damage " + hp)
          val newHp = state.currentHitPoints + hp
          if (newHp * 2 < state.baseHitPoints) Blodied(newHp, state.baseHitPoints)
          else if (newHp >= state.baseHitPoints) new Healthy(newHp, state.baseHitPoints)
          else new Healthy(newHp, state.baseHitPoints)
      }
    }
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    val damage = Events("damage")
    val heal = Events("heal")

    println(damage(new Healthy(10, 10)))
    println(heal(new Dead(10)))
    println(heal(new Healthy(1, 20)))
    println(heal(new Healthy(10, 20)))
    println(heal(new Healthy(10, 20)))
  }
}
