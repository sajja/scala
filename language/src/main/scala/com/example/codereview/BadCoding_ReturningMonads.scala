package com.example.codereview

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BadCoding_ReturningMonads {
  def exampleBad() = {
    for {
      wsReturnVal <- webserviceCall1(1)
      calc <- calculate(wsReturnVal)
      wsReturnVal <- webserviceCall2(calc)
    } yield {}
  }

  def calculate(i: Int) = {
    Future {
      i * 10 / 2
    }
  }

  def webserviceCall1(i: Int) = {
    Future {
      Thread.sleep(1000)
      10
    }
  }

  def webserviceCall2(i: Int) = {
    Future {
      Thread.sleep(1000)
      i * 100
    }
  }

  def refactor1() = {
    for {
      wsReturnVal <- webserviceCall1(1)
      wsReturnVal <- calculateAndWebserviceCall2(wsReturnVal)
    } yield {}
  }

  def refactor2() = {
    for {
      wsReturnVal <- webserviceCall1(1)
      cal <- Future(betterCalculateMethod(wsReturnVal))
      wsReturnVal <- webserviceCall2(cal)
    } yield {}
  }

  def betterCalculateMethod(i: Int) = {
    i * 10 / 2
  }

  def calculateAndWebserviceCall2(i: Int) = {
    val j = betterCalculateMethod(i)
    Future {
      Thread.sleep(1000)
      j * 100
    }
  }
}
