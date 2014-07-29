package com.example

import akka.actor.ActorSystem
import com.example.json.{Address, Person}
import spray.client.pipelining.{Get, sendReceive, _}
import spray.http.HttpHeaders.Authorization
import spray.http.{BasicHttpCredentials, HttpRequest, HttpResponse}
import spray.httpx.SprayJsonSupport
import spray.httpx.encoding.Deflate
import spray.json.{DefaultJsonProtocol, JsonFormat}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait WebClient {
  def get(url: String): Future[String]
}

case class Invoice(id: String, x: String)

//case class Location1(lat: Double, lng: Double)
case class GoogleApiResult1[T](id: String, results: String)
//

import com.example.ElevationJsonProtocol._
import spray.httpx.SprayJsonSupport._

// implementation of WebClient trait
class SprayWebClient(implicit system: ActorSystem) extends WebClient {

  import system.dispatcher

  // create a function from HttpRequest to a Future of HttpResponse
  //  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
  //  val pipeline = addCredentials(BasicHttpCredentials("sajiths", "123456"))~> sendReceive ~> unmarshal[GoogleApiResult1[Invoice]]

  implicit val aC = jsonFormat2(Address)
  implicit val pC = jsonFormat2(Person)
  val pipeline: HttpRequest => Future[HttpResponse] = (
    addHeader("X-My-Special-Header", "fancy-value")
      ~> addCredentials(BasicHttpCredentials("sajiths", "123456"))
//      ~> decode(Deflate)
      ~> sendReceive
//      ~> unmarshal[Person]
    )

  //
  // create a function to send a GET request and receive a string response
  def get(url: String): Future[String] = {
    val get = Get(url)
    val futureResponse = pipeline(Get(url))
    futureResponse.map(_.entity.asString)
  }
}

object Program extends Application {
  //  import system.dispatcher // execution context for futures

  // bring the actor system in scope
  implicit val system = ActorSystem()

  // create the client
  val webClient = new SprayWebClient()(system)

  val auth = Authorization(BasicHttpCredentials("sajiths", "123456"))
  // send GET request with absolute URI
  val futureResponse = webClient.get("http://localhost:8080/g")
  //  val futureResponse = webClient.get("https://10.2.4.106/api/v1/document/INVOICE:600022408")
  //  val futureResponse = webClient.get("http://127.0.0.1:8080/g")
  // wait for Future to complete

  import com.example.Program.system.dispatcher

  futureResponse onComplete {
    case Success(response) => {
      println(response)
    }
    case Failure(error) => {
      println("An error has occured: " + error.getMessage)
    }
  }
}
