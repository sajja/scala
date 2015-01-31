package com.example.actors.resturant

import java.util.Random

import akka.actor._

case class LookingForWaiter(client: ActorRef)

case class DispatchWaiter(client: ActorRef)

case class WaiterReady(bill: Bill)

case class Order(item: String, bill: Bill)

case class ReOrder(client: ActorRef, item: String)

case class ItemServed(item: String, bill: Bill)

case class ItemNotAvailable

case class ItemConsumed(item: String, bill: Bill)

case class Bill(itemsConsumed: Seq[String])

class Client(val delay: Int, val resturant: ActorRef, foodType: String) extends Actor {
  val items = Array("BEER", "PIZZA", "BILL")

  override def receive: Actor.Receive = {
    case WaiterReady(bill) => {
      println("Client: Let me think for a sec...")
      Thread.sleep(delay)
      val food = items(new Random().nextInt(3))
      println("Client: Ok... I want " + food)
      sender ! Order(food, new Bill(bill.itemsConsumed :+ food))
    }
    case ItemServed(item,bill) => {
      println("Client: Thank you very much..")
      Thread.sleep(delay * 3)
      println("Client: I'm done eating...")
      sender ! ItemConsumed(item,bill)
    }
    case ItemNotAvailable => println("Client: Oh bummer ....")
  }
}

class Waiter extends Actor {
  override def receive: Receive = {
    case DispatchWaiter(client) => {
      println("Waiter: Going to the client ....")
      Thread.sleep(2000)
      println("Waiter: Hello sir what would you want to order ....")
      client ! WaiterReady(Bill(Seq[String]()))
    }
    case ItemConsumed(item, bill) => {
      println("Waiter: Going to to client to pick up leftovers")
      Thread.sleep(2000)
      println("Waiter: Do you want to order anything ...")
      sender ! WaiterReady(bill)
    }
    case Order("BEER", bill) => {
      println("Waiter: Ok sir BEER coming up....")
      Thread.sleep(2000)
      println("Waiter: Here you go sir... enjoy")
      sender ! ItemServed("BEER", bill)
    }
    case Order("PIZZA", bill) => {
      println("Waiter: Ok sir I will make a PIZZA for you. It would take few min..")
      println("Waiter: Here you go sir... enjoy")
      sender ! ItemServed("PIZZA",bill)
    }
    case Order("BILL", bill) => {
      println("Waiter: Sure I'll give you the bill right away")
      Thread.sleep(2000)
      println("Waiter: Here is the bill sir...")
      bill.itemsConsumed.foreach(println)
    }
    case Order(i,_) => {
      println("Waiter: I'm sorry sir we don't have " + i)
      sender ! ItemServed
    }
  }
}

class Resturant(implicit system: ActorSystem) extends Actor {
  val waiter = system.actorOf(Props[Waiter])

  override def receive: Actor.Receive = {
    case LookingForWaiter(client) => waiter ! DispatchWaiter(client)
  }
}

class Kitchen extends Actor {
  override def receive: Actor.Receive = {
    case Order(i,_) =>
  }
}

object MyResturent {
  def main(args: Array[String]) {
    val system = ActorSystem("Resturant")
    val resturent = system.actorOf(Props(classOf[Resturant], system), "Resturent")
    val beerClient = system.actorOf(Props(classOf[Client], 3000, resturent, "BEER"), "client1")
    val pizzaClient = system.actorOf(Props(classOf[Client], 3000, resturent, "Pizza"), "client2")

    resturent ! LookingForWaiter(beerClient)

    resturent ! LookingForWaiter(pizzaClient)

    val x = new Random()
    system.awaitTermination()
  }
}