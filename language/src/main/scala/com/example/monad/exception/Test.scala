package com.example.monad.exception

import com.example.monad.exception.v1._
import com.example.monad.exception.v2.{TService3, TService4, TService2, TService1}

import scala.util.Failure


case class Service1Exception() extends Exception
case class Service2Exception() extends Exception
case class Service3Exception() extends Exception
case class Service4Exception() extends Exception
object Test {

  def oldWay ={
    try {
    val s1 = Service1.invoke(10)

    val s2 = Service2.invoke(s1)
    val s3 = Service3.invoke(1000)
    val s4 = Service4.invoke(10000)
    println(s4)
    } catch {
      case e:Service1Exception => println("nothign to rollback")
    }
  }

  def newWay = {
    val s4 = for {
      a <-TService1.invoke(0)
      b <-TService2.invoke(a)
      c <-TService3.invoke(1000)
      d <-TService4.invoke(1000).recover{
        case e:Service1Exception => println("xxx");Failure(e)
      }
    } yield d

    println(s4)
  }

  def main(args: Array[String]): Unit = {
//    oldWay
    newWay
  }
}
