package com.example.actors.streams.test1

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Actor, Props, ActorSystem}

class SlowActor extends Actor {
  override def receive: Receive = {
    case _ =>
      Thread.sleep(1000)
      print(".")
      sender ! "Done..."
  }
}


class FastPublisherActor(val subscriber: ActorRef) extends Actor {

  override def receive: Actor.Receive = {
    case i: Int =>
      (1 to i).foreach(_ => subscriber ! "Ok")
    case _ =>
  }
}

/**
  * Created by sajith on 5/22/17.
  */
object SlowActorTest {
  def main(args: Array[String]) {

    val system = ActorSystem("SlowActorSystem")
    val slowActor = system.actorOf(Props[SlowActor], name = "slowActor")
    val publisher = system.actorOf(Props(new FastPublisherActor(slowActor)), "publisher")
    publisher ! 100000

  }
}
