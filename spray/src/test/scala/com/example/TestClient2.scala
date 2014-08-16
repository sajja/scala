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


object MyImplicits extends DefaultJsonProtocol {
  implicit val aC = jsonFormat2(Address)
  implicit val pC = jsonFormat2(Person)
}

object Main extends App {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("simple-spray-client")
  import system.dispatcher // execution context for futures below
  val log = Logging(system, getClass)

  log.info("Calling the rest api")

  import MyImplicits._
  import SprayJsonSupport._
  val pipeline = addCredentials(BasicHttpCredentials("sajiths", "123456")) ~> sendReceive ~> unmarshal[Person]

  val responseFuture = pipeline.apply(Get("http://localhost:8080/g"))

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