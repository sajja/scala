package com.example.codereview

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BadCoding_IndependentFutures {

  def exampleBad(i: Int, j: Int) = {
    for {
      _ <- Future(i * 100)
      _ <- Future(j * 100)
    } yield {}
  }

  def refactor(i: Int, j: Int) = {
    val r1 = Future(i * 100)
    val r2 = Future(j * 100)
    for {
      _ <- r1
      _ <- r2
    } yield {}
  }
}
