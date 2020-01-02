package com.example.futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object StacktraceTest {

  def func1(): Future[Unit] = {
    println("IN FUNC1")
    func2()
  }

  def func2(): Future[Unit] = {
    println("IN FUNC2")
    func3()
  }

  def func3(): Future[Unit] = {
    println("IN FUNC3")
    func4()
  }

  def func4(): Future[Unit] = {
    println("IN FUNC3")
    Future {throw new Exception("exception")}
  }

  def main(args: Array[String]): Unit = {
    val o = StacktraceTest
    Await.result(o.func1(),Duration.Inf)
  }
}
