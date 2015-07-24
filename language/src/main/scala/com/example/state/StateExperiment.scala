package com.example.state

class State()

case class HealthyState() extends State

case class DeadState() extends State

case class BlodiedState() extends State

class Warrior(val hp: Int, val ac: Int, val state: State)

object StateTest {
  def transform2[A, B](prevStaet: A)(f: A => B) = {
    f(prevStaet)
  }

  def printState(state: State) = println(state)

  def attack(warrior: Warrior, damage: Int) = {
   
  }

  def main(args: Array[String]): Unit = {
    val p1 = new Warrior(100, 10, new HealthyState)

  }
}
