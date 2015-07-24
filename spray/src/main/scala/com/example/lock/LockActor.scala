package com.example.lock

import akka.actor.{Props, ActorSystem, Actor}
import akka.actor.Actor.Receive
import java.util.concurrent.TimeUnit
import com.example.json.{Person, Address}
import shapeless.Succ
import spray.client.pipelining._
import spray.http.{Uri, HttpRequest, BasicHttpCredentials}
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, Duration}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Try, Success, Failure, Random}

case class Acquire(f: () => Unit)

case class Execute(f: () => Unit)

case class Update()


case class Method(action: String, node: Node)

case class Node(key: String, value: String, modifiedIndex: Int, createdIndex: Int)

object MyImplicits extends DefaultJsonProtocol {
  implicit val nodeFormat = jsonFormat4(Node)
  implicit val methodFormat = jsonFormat2(Method)
}

trait Lock {
  def acquire(id: String): Boolean

  def hasLock(id: String): Boolean
}

class EtcdLock extends Lock {
  override def acquire(id: String): Boolean = {
    //make a put request
    val args = Map(
      "value" -> id,
      "prevExist" -> "false"
    )
    val uri = Uri(s"http://127.0.0.1:2379/v2/keys/mykey").withQuery(args)
    withPipeline(Put(uri))
  }

  def xxx(f: => Unit) = {
    import MyImplicits._
    import SprayJsonSupport._

    implicit val system = ActorSystem("simple-spray-client")
    val pipeline = sendReceive ~> unmarshal[Method]

     val args = Map(
      "value" -> "",
       "ttl" -> "10",
      "prevExist" -> "false"
    )
    val uri = Uri(s"http://127.0.0.1:2379/v2/keys/mykey").withQuery(args)
    system.scheduler.schedule(1 milliseconds, 100 milliseconds, new Runnable {
      override def run(): Unit = {
        println("Hello")
      }
    })
    withPipeline(Put(uri))
  }

  def withPipeline(op: HttpRequest) = {
    import MyImplicits._
    import SprayJsonSupport._

    implicit val system = ActorSystem("simple-spray-client")
    val pipeline = sendReceive ~> unmarshal[Method]

    Try(Await.result(pipeline.apply(op), 900 milliseconds)) match {
      case Success(e) => {
        println(e.node.value)
        true
      }
      case Failure(e) => {
        e.printStackTrace()
        false
      }
    }
  }

  override def hasLock(id: String): Boolean = {
    withPipeline(Get("http://127.0.0.1:2379/v2/keys/mykey"))
  }
}

class DummyLock extends Lock {
  override def acquire(id: String): Boolean = {
    new Random().nextBoolean()
  }

  override def hasLock(id: String): Boolean = new Random().nextBoolean()
}

class UpdateLock extends Actor {
  override def receive: Receive = {
    case Update() => {
      println("updating lock")
      context.stop(self)

    }
  }
}

class LockActor extends Actor {
  override def receive: Receive = {
    case Acquire(f) => {
      val res = new DummyLock().acquire("")
      if (res) {
        self ! Update
        self ! Execute(f)
      } else {
        self ! Acquire(f)
      }
    }
    case Execute(f) => {
      if (new DummyLock().hasLock("")) {
        Thread.sleep(1000)
        f()
      } else {
        println("trying to get lock ...")
        self ! Acquire(f)
      }
      self ! Execute(f)
    }
    case Update => {
      println("Updating .....")
      Thread.sleep(100)
      self ! Update
    }
  }
}


object LockTest {
  def main(args: Array[String]): Unit = {
    //    val system = ActorSystem("Resturant")
    //    val lock = system.actorOf(Props(classOf[LockActor]), "Lock")
    //    lock ! Acquire(() => println("Got lock. Doing something with it...."))
    val etc = new EtcdLock
//    println(etc.acquire("1"))
    etc.xxx{


    }
//    System.exit(0)
  }
}
