package com.example.statemachine

import akka.actor.Address
import com.example.generics.variance.Employee
import com.example.statemachine.Client.{ContactAction, EmployeeAction}

import scalaz.{Coproduct, Free}

class MyFSM[I, S](val state: S, f: (S, I) => S) {
  def runState(i: I): MyFSM[I, S] = {
    new MyFSM[I, S](f(state, i), f)
  }
}


class AbstactState(val i: Int)

class OpenState(i: Int) extends AbstactState(i)

class CloseState(i: Int) extends AbstactState(i)

class BeerMachine(val coins: Int, val beer: Int) {
  override def toString: String = s"Beers ${beer} Coins ${coins}"

  def dispense(): BeerMachine = {
    if (beer > 0 && coins > 0) {
      println(s"Machine: Dispensing Beer")
      if (beer == 1)
        OutOfBeerMachine(coins - 1)
      else if (coins == 1)
        NoCoinMachine(beer)
      else
        OpenMachine(coins - 1, beer - 1)
    } else {
      println(s"Cannot dispense. Beers: $beer Coins $coins")
      this
    }
  }
}

case class OpenMachine(override val coins: Int, override val beer: Int) extends BeerMachine(coins, beer)

case class NoCoinMachine(override val beer: Int) extends BeerMachine(0, beer)

case class OutOfBeerMachine(override val coins: Int) extends BeerMachine(coins, 0)

class Operation()

case class InsertCoin() extends Operation

case class PullCrank() extends Operation

object Client {
  def printState[I, S](myFSM: MyFSM[I, S]) = {
    println(myFSM.state.toString)
  }

  def main(args: Array[String]): Unit = {
    val beerMachine = new MyFSM[Operation, BeerMachine](NoCoinMachine(3),
      (machine, operation) => (operation, machine) match {
        case (InsertCoin(), machine: OutOfBeerMachine) =>
          println("Machine: Sorry i'm out of beer")
          machine
        case (InsertCoin(), _) =>
          println("Machine: Coin inserted")
          OpenMachine(machine.coins + 1, machine.beer)
        case (PullCrank(), _: NoCoinMachine) =>
          println("Machine: Insert a coin first")
          machine
        case (PullCrank(), _: OpenMachine) =>
          machine.dispense()
        case (PullCrank(), machine: OutOfBeerMachine) =>
          println("Machine: Sorry i'm out of beer")
          machine
      })

    val s1 = beerMachine.runState(PullCrank())
    val s2 = s1.runState(InsertCoin())
    val s3 = s2.runState(PullCrank())
    printState(s3)
    val s4 = s3.runState(InsertCoin())
    val s5 = s4.runState(PullCrank())
    printState(s5)
    val s6 = s5.runState(InsertCoin())
    val s7 = s6.runState(PullCrank())
    printState(s7)
    val s8 = s7.runState(InsertCoin())
    val s9 = s8.runState(PullCrank())
    printState(s9)
  }
}