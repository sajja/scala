package com.example.classes

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by sajith on 7/28/14.
  */
class Animal(val name: String) {
}

class Dog(name: String) extends Animal(name) {
}

case class PersonName(first: String, last: String, middle: Option[String])

object PersonName {
  def apply(first: String, last: String): PersonName = {
    new PersonName(first, last, None)
  }
}

object Test {
  val me = PersonName("Jessica", "Kerr")
  //  val you = PersonName("Jessica", "Kerr", Option("ddd"))
  val golanguri = "http://localhost:9090"
  val nodeuri = "http://localhost:8080"

  //  val greeting = me match {
  //    case PersonName(first, last) => s"Hello, $first $last"
  //  }

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val start = System.nanoTime()
    val z = for (i <- 0 to 1000) yield {
      Future {
        val client = HttpClientBuilder.create().build()
        val get = new HttpGet(nodeuri)
        client.execute(get)
      }
    }
    val xx = Future.sequence(z)
    Await.result(xx, Duration.Inf)
    val end = System.nanoTime()
    println(end - start)
  }
}