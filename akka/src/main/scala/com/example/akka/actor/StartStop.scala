package com.example.akka.actor

import java.lang.Error
import java.util.logging.{Level, Logger}

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class StartStopActor1 extends Actor {
  override def preStart(): Unit = {
    println("first started")
    context.actorOf(Props[StartStopActor2], "second")
  }

  override def postStop(): Unit = println("first stopped")

  override def receive: Receive = {
    case "stop" => context.stop(self)
  }
}

class StartStopActor2 extends Actor {
  override def preStart(): Unit = println("second started")

  override def postStop(): Unit = println("second stopped")

  // Actor.emptyBehavior is a useful placeholder when we don't
  // want to handle any messages in the actor.
  override def receive: Receive = Actor.emptyBehavior
}

object StartStopActor1 extends App {
  val system = ActorSystem("input")
  val first = system.actorOf(Props[StartStopActor1], "first")
  first ! "stop"
}