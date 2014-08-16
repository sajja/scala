package com.example

import akka.actor.Actor
import com.example.json.{Person, Address}
import spray.http.{HttpEntity, ContentTypes}
import spray.json._
import spray.routing._
import spray.json.DefaultJsonProtocol
import DefaultJsonProtocol._ // !!! IMPORTANT, else `convertTo` and `toJson` won't work correctly
import spray.http._

object JsonImplicits extends DefaultJsonProtocol {
  implicit val impPerson = jsonFormat2(Address)
}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

//// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  val x = {
      put {
        complete{
          <html></html>
        }
      }
    }

  val myRoute = path("g")(x)
//  {
//    put {
//      complete{
//        <html></html>
//      }
//    }
//  }
}



/*
trait MyService extends HttpService {
  val myRoute =
    path("g") {
      put {
        respondWithMediaType(MediaTypes.`text/html`) {
          complete {
            <html>
              <body>
                <h1>put done</h1>
              </body>
            </html>
          }
        }
      } ~ get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            val address = new Address(41, "backer street")
            val emp = new Person("sherlock holmns", address)

            implicit val addC = jsonFormat2(Address)
            implicit val addP = jsonFormat2(Person)
            HttpEntity(emp.toJson.prettyPrint)
          }
        }
      }
    }
}
 */