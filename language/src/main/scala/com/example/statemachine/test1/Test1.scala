package com.example.statemachine.test1

object StateTest1 {

  case class State[S](val s: S)

  object StateTransform {
    def apply[A, B](s: State[A], f: A => State[B]): State[B] = {
      f(s.s)
    }
  }

  case class World(val obj: Int)

  def main(args: Array[String]): Unit = {
    val world = State(World(10))
    val newWorld = StateTransform[World, World](world, (s) => State(World(s.obj * 2)))
    println(newWorld.s)
  }
}


