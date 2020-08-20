package com.example.statemachine.beermachineOO

abstract class State() {
  def dispense()

  def inserCoin()
}

class Action()


case class Empty() extends State {
  override def dispense(): Unit = println("Cannot dispense, no beers")

  override def inserCoin(): Unit = println("Cannot accept coins, no beers")
}

case class Idle(bm: BeerMachineOO) extends State {
  override def dispense(): Unit = println("Please put coins first")

  override def inserCoin(): Unit = {
    println("Coin accepted. Ready to dispense")
    bm.state = Ready(bm)
  }
}

case class Ready(bm: BeerMachineOO) extends State {
  override def dispense(): Unit = {
    println("Here is your beer")
    bm.beers = bm.beers - 1
    bm.state = if (bm.beers == 0) Empty() else Idle(bm)
  }

  override def inserCoin(): Unit = println("System ready to dispense, cannot accept coins")
}

case class InsertCoin() extends Action

case class Dispense() extends Action


class BeerMachineOO(var beers: Int) {

  var state: State = Idle(this)

  def action(action: Action) = {
    action match {
      case Dispense() => state.dispense()
      case InsertCoin() => state.inserCoin()
    }
  }

  def main(args: Array[String]) {

  }
}


object TestBeersOO {
  def main(args: Array[String]) {
    val bm = new BeerMachineOO(2)
    bm.action(Dispense())
    bm.action(InsertCoin())
    bm.action(InsertCoin())
    bm.action(Dispense())

    bm.action(InsertCoin())
    bm.action(Dispense())
    bm.action(InsertCoin())
    bm.action(Dispense())
    bm.action(InsertCoin())
    bm.action(Dispense())
  }
}