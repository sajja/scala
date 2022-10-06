package com.example.futures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureSeq {

  def getFuture(i: Int) = {
    if (i % 2 == 0) Future.failed(new Exception("dddddd"))
    else Future.successful(Some(i))
  }

  def main(args: Array[String]): Unit = {
    val seqFut = for (i <- 0 to 10) yield {
      getFuture(i).flatMap {
        case Some(i)=>Future(""+i)
        case _ => Future("^^")
      }
    }
    println(seqFut)
  }
}
