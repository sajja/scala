package com.example.monad.horseman.uncertinity

case class Knight(c: Int, r: Int) {
  def move = {
    List(Knight(c + 2, r + 1), Knight(c + 2, r - 1))
  }
}

case class Casel(c: Int, r: Int) {
  def move = {
    List(Casel(c, r + 1), Casel(c, r + 2), Casel(c, r + 3))
  }
}

class NonDet[T](t: T) {
  def map[B](f: T => B): NonDet[B] = new NonDet[B](f(t))

  def flatMap[B](f: T => NonDet[B]): NonDet[B] = {
    f(t)
  }

  Option
}

object UncertinityTest extends App {
  def knightMove(c: Int, r: Int) = (c, r) match {
    case (_, _) if c > 0 && r > 0 => List((c + 2, r + 1), (c + 2, r - 1))
    case (_, _) if r == 0 => List((c + 2, r + 1))
  }

  def caselMove(c: Int, r: Int) = List((c, r + 1), (c, r + 2), (c, r + 3))

  def knightMove1(c: Int, r: Int) = new NonDet[List[(Int, Int)]]((c, r) match {
    case (_, _) if c > 0 && r > 0 => List((c + 2, r + 1), (c + 2, r - 1))
    case (_, _) if r == 0 => List((c + 2, r + 1))
  })

  def caselMove1(c: Int, r: Int) = new NonDet[List[(Int, Int)]](List((c, r + 1), (c, r + 2), (c, r + 3)))

  val l = List(1)
  val m1 = knightMove1(0, 0)
}
