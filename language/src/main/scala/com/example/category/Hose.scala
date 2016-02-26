package com.example.category

/**
  * Created by sajith on 2/21/16.
  */

trait Interface {
  def verbose()
}

trait Hose[IN <: Interface, OUT <: Interface] {
  def verbose(): String

  def compose(hose: Hose[IN, OUT]): Hose[IN, OUT]

  def len(): Int
}


class SmallInterface(name: String) extends Interface {
  override def verbose(): Unit = s"Interface: $name"
}
class BigInterface(name: String) extends Interface {
  override def verbose(): Unit = s"Interface: $name"
}

class RoundHose(l: Int) extends Hose[SmallInterface, SmallInterface] {
  override def len() = l

  override def verbose(): String = s"length $len"

  override def compose(hose: Hose[SmallInterface, SmallInterface]): Hose[SmallInterface, SmallInterface] = new RoundHose(hose.len + l)
}

class BigHose(l:Int) extends Hose[BigInterface, BigInterface] {
  override def verbose(): String = ???

  override def compose(hose: Hose[BigInterface, BigInterface]): Hose[BigInterface, BigInterface] = new BigHose(10)

  override def len(): Int = ???
}


object TestHose {
  def main(args: Array[String]) {
    val TenMeterHose = new RoundHose(10)
    val TwoMeterHose = new RoundHose(2)
    val bigHose = new BigHose(10)
    println(TwoMeterHose.compose(TenMeterHose).verbose())
//    TenMeterHose.compose(bigHose) //types dont line up cannot compile.

  }
}
