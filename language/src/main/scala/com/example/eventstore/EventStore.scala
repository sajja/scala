package com.example.eventstore

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props, ActorSystem}
import akka.pattern.AskableActorRef
import akka.util.Timeout
import scala.concurrent.duration._

import scala.concurrent.{Awaitable, Future, Await}

case class Account(no: String, balance: Int, version: Int)

case class CreateAccountCommand(no: String, balance: Int)

case class AccountCreatedEvent(no: String, balance: Int)

case class WithdrawAccount(no: String, amount: Int)

case class ListAccountsEvent()

class EventStore(val readStoreActor: ActorRef) {
  implicit val timeout = Timeout(2, TimeUnit.SECONDS)

  import akka.pattern.ask

  def findAccount(no: String) = Await.result(readStoreActor ? no, 1000 second) match {
    case Some(a: Account) => Some(a)
    case _ => None
  }

  def createAccount(no: String, balance: Int) = {
    println(s"Tring to create account $no")
    findAccount(no) match {
      case None => readStoreActor ! AccountCreatedEvent(no, balance)
      case Some(_) => throw new Exception("Account exists")
    }
  }

  def withdraw(no: String, amount: Int) = {
    findAccount(no) match {
      case Some(Account(accNO, blance, _)) => readStoreActor ! WithdrawAccount(accNO, amount)
    }
  }

  def listAccounts() = Await.result(readStoreActor ? ListAccountsEvent(), 1000 second)
}

class ReadStoreActor extends Actor {
  var accounts: Map[String, Account] = Map()

  //  def createAccount(no: String, balance: Int) = if (findAccount(no).isEmpty) accounts += (no -)

  def updateAccount(account: Account) = if (findAccount(account.no).isDefined) accounts += (account.no -> account)

  def findAccount(no: String): Option[Account] = accounts.get(no)

  override def receive: Receive = {
    case AccountCreatedEvent(no, balance) => if (findAccount(no).isEmpty) accounts += (no -> Account(no, balance, 0))
    case ListAccountsEvent() => sender ! accounts.values.toList
    case WithdrawAccount(no, amount) => {
      val account = for {
        account <- findAccount(no)
      } yield {
        accounts += (no -> Account(no, account.balance - amount, account.version + 1))
      }
    }
    case no: String => sender ! findAccount(no)
  }
}

object TestEventStroe {
  def main(args: Array[String]) {
    val system = ActorSystem("EventStore")
    val readStore = system.actorOf(Props[ReadStoreActor])
    val commandModule = new EventStore(readStore)
    commandModule.createAccount("1", 100)
    commandModule.createAccount("2", 200)
    commandModule.createAccount("3", 300)
    commandModule.withdraw("3", 200)
    println(commandModule.listAccounts())
    system.shutdown()
  }
}