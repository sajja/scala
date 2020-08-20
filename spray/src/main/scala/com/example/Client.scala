package com.example

import java.net.InetSocketAddress

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem}
import akka.io.{Tcp, Udp, IO}
import com.example.json.{Address, Person}
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import SprayJsonSupport._
import DefaultJsonProtocol._ // !!! IMPORTANT, else `convertTo` and `toJson` won't work correctly
/**
  * Created by sajith on 8/4/16.
  */
object Client {
  def main(args: Array[String]) {
    import spray.client.pipelining._
    implicit val system = ActorSystem()
    import system.dispatcher
    implicit val addC = jsonFormat2(Address)
    implicit val addP = jsonFormat2(Person)
    val pipeline = sendReceive ~> unmarshal[Person]
//    val pipeline = sendReceive
    val response = pipeline(Get("http://localhost:8080/g"))
    println(Await.result(response, 10 seconds))
  }
}

class SimpleClient(destAddress: InetSocketAddress) extends Actor {

  override def receive: Actor.Receive = ???
}
