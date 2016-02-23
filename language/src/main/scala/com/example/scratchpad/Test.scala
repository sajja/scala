package com.example.scratchpad

case class Connection()


object Test {

  type X[Y] = Connection => Y

  def t1(c: Connection): Int = {
    println(c)
    100
  }

  def intOp: X[Int] = (c: Connection) => 100

  def t2(c: Connection): Boolean = {
    println(c)
    true
  }

  def add(x: Int) = x + 42

  def map[A, B](x: X[A])(f: A => B): X[B] = c => f(x(c))

  def main(args: Array[String]): Unit = {
    println(map[Int, String](c => 100)(i => s"xx $i")(Connection()))
  }
}


