package com.example.actors.resturant

import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.Duration

case class ClientReadyToOrder(client: ActorRef)

case class DispatchWaiter(client: ActorRef)

case class WaiterBusy()

case class WaiterIdle()

case class Order(item: String)

case class OrderServed(item: String)

case class WaiterReadyToTakeOrder() {
  val menu = Array("PIZZA", "BEER", "BILL")
}

class Waiter(i: Int, system: ActorSystem) extends Actor {
  override def receive: Actor.Receive = {
    case DispatchWaiter(client) => {
      println("Waiter: Hello I'm waiter " + i + " What you wanna order")
//      system.actorSelection("user/MyResturant") ! WaiterBusy
      client ! WaiterReadyToTakeOrder
    }
    case Order(item: String) =>
      println("Waiter: One " + item + "coming up")
      Thread.sleep(3000)
      println("Waiter: Here you go sir.. One " + item)
      sender ! OrderServed(item)
      system.actorSelection("user/MyResturant") ! WaiterIdle
  }
}

class Client extends Actor {
  override def receive: Actor.Receive = {
    case WaiterReadyToTakeOrder =>
      println("Client: Mmmmmm.. Let me think")
      Thread.sleep(4000)
      println("Client: Ok I got it. I want Beer")
      sender ! Order("BEER")
    case OrderServed(item: String) =>
      println("Client: Thank you.. I'll call you once I'm Done with the " + item)
      Thread.sleep(5000)
  }
}

class Restaurant(system: ActorSystem) extends Actor {
  var open = false
  val waiter = system.actorOf(Props(classOf[Waiter], 1, system))
  var availableWaiterCount = 1

  override def receive: Receive = {
    case ClientReadyToOrder(client) => {
      if (availableWaiterCount > 0) {
        waiter ! DispatchWaiter(client)
        sender ! "Waiter will be comming right up"
        availableWaiterCount -= 1
      } else {
        sender ! "I'm very sorry. All waiters are busy now"
      }
    }

    case WaiterIdle => availableWaiterCount += 1
  }
}

object MyResturent {
  def main(args: Array[String]) {
    val system = ActorSystem("Resturant")
    val restaurant = system.actorOf(Props(classOf[Restaurant], system), "MyResturant")
    val client1 = system.actorOf(Props[Client], "Client1")
    val client2 = system.actorOf(Props[Client], "Client2")
    implicit val timeout = Timeout(2,TimeUnit.SECONDS)
    implicit val ec = system.dispatcher

    (restaurant ? ClientReadyToOrder(client1)).onSuccess{
      case x:String=>println(x)
    }

    (restaurant ? ClientReadyToOrder(client2)).onSuccess{
      case x:String=>println(x)
    }

    Thread.sleep(10000)
    println("Angry client re-orders....")
    (restaurant ? ClientReadyToOrder(client2)).onSuccess{
      case x:String=>println(x)
    }
    system.awaitTermination(Duration(80L, "s"))
  }
}