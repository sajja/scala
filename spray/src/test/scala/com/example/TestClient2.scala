package com.example

import com.example.json.{Person, Address}
import spray.http.BasicHttpCredentials

import scala.util.{Success, Failure}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.event.Logging
import akka.io.IO
import spray.json.{JsonFormat, DefaultJsonProtocol}
import spray.can.Http
import spray.httpx.SprayJsonSupport
import spray.client.pipelining._
import spray.util._
import spray.json.DefaultJsonProtocol

import spray.json.DefaultJsonProtocol
import DefaultJsonProtocol._ // !!! IMPORTANT, else `convertTo` and `toJson` won't work correctly
import spray.json._

case class M(action:String, node:Node)
case class Node(key:String, value:String, modifiedIndex:Int, createdIndex:Int)

object MyImplicits extends DefaultJsonProtocol {
  implicit val aC = jsonFormat2(Address)
  implicit val pC = jsonFormat2(Person)
    implicit val xx = jsonFormat4(Node)
    implicit val x1 = jsonFormat2(M)
//  implicit val xx1 = jsonFormat2(Node1)
}

object Main extends App {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("simple-spray-client")
  import system.dispatcher // execution context for futures below
  val log = Logging(system, getClass)

  log.info("Calling the rest api")

  import MyImplicits._
  import SprayJsonSupport._
//  val pipeline = addCredentials(BasicHttpCredentials("sajiths", "123456")) ~> sendReceive ~> unmarshal[Person]
  val pipeline = addCredentials(BasicHttpCredentials("sajiths", "123456")) ~> sendReceive ~> unmarshal[M]

  val responseFuture = pipeline.apply(Get("http://127.0.0.1:2379/v2/keys/mykey"))

  val dd = "ddd"
  responseFuture onComplete {
    case Success(e) =>
      log.info(e.toString)
      shutdown()

    case Success(somethingUnexpected) =>
      log.warning("The Google API call was successful but returned something unexpected: '{}'.", somethingUnexpected)
      shutdown()

    case Failure(error) =>
      log.error(error, "Couldn't get elevation")
      shutdown()
  }
//
  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1.second).await
    system.shutdown()
  }
}