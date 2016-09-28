package com.example.statemachine.beermachineFun


class Action()

case class InserCoin() extends Action

case class Dispense() extends Action

trait BeerMachineFunctional {

  case class State[S](run: (S) => (S))

  case class BeerMachine(val beers: Int, val coin: Int)

  object BeerMachine {
    def stateTransition(machine: BeerMachine, action: Action): BeerMachine = {
      action match {
        case InserCoin() if machine.beers > 0 => BeerMachine(machine.beers, machine.coin + 1)
        case Dispense() if machine.beers > 0 && machine.coin > 0 => BeerMachine(machine.beers - 1, machine.coin - 1)
        case _ => machine
      }
    }

    def apply[A <: Action](actions: List[A]): State[BeerMachine] = {
      State[BeerMachine]((machine: BeerMachine) => {
        actions.foldLeft(machine)(stateTransition)
      })
    }

    def apply[A <: Action](action: A): State[BeerMachine] = apply(List(action))
  }

  def beerMachine = BeerMachine(10, 0)
}

object BeerMachineFunctionalTest {
  def main(args: Array[String]) {
    val sys = new BeerMachineFunctional {}
    val out = sys.BeerMachine(List(InserCoin(), InserCoin(), InserCoin(), Dispense())).run(sys.beerMachine)
    println(s"beers ${out.beers} ${out.coin}")
  }
}
