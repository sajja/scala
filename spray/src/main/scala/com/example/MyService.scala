package com.example

import _root_.akka.actor.Actor
import _root_.akka.actor.ActorSystem
import _root_.akka.actor.Props
import _root_.akka.io.IO
import akka.actor.Actor.Receive
import akka.actor._
import akka.io.IO
import shapeless.get
import spray.can.Http
import spray.can.Http
import spray.http.{Uri, HttpRequest}
import spray.routing.HttpService
import spray.routing.SimpleRoutingApp
import spray.routing.{HttpService, SimpleRoutingApp}
import spray.util.SprayActorLogging

object Main extends App {
  implicit val system = ActorSystem("my-system")

  val service = system.actorOf(Props[SpraySampleActor], "spray-sample-service")
  IO(Http) ! Http.Bind(service, "localhost", 8080)
}

class BasicActor extends Actor {
  override def receive: Receive = {
    case HttpRequest(_,Uri.Path("/basic"),_,_,_)=>sender ! "Basic actor response"
  }
}

class SpraySampleActor extends Actor with SpraySampleService {
  def actorRefFactory = context
  def receive = runRoute(spraysampleRoute2)
}

trait SpraySampleService extends HttpService {
  val spraysampleRoute1 = {
    path("entity" / Segment) { id =>
      get {
        complete(s"list $id")
      } ~
        post {
          complete("create")
        }
    }
  }
  val spraysampleRoute2 = {
    path("entity") {
      get {
        complete(s"list ")
      }
    }
  }

}
