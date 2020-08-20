package com.example.monad.exception.v2

import com.example.monad.exception.{Service4Exception, Service3Exception, Service2Exception, Service1Exception}

import scala.util.{Failure, Try}

object TService1 {
  def invoke(i:Int) :Try[Int] = {
    if (i == 0)
      return Failure[Int](new Service1Exception)
    Try(i)
  }
  def rollbak()={}
}

object TService2 {
  def invoke(i:Int):Try[Int] = {
    if (i == 10)
      return Failure[Int](new Service2Exception)
    Try(i)
  }
  def rollbak()={}
}

object TService3 {
  def invoke(i:Int) :Try[Int] = {
    if (i == 100)
      return Failure[Int](new Service3Exception)
    Try(i)
  }
  def rollbak()={}
}

object TService4 {
  def invoke(i:Int) :Try[Int] = {
    if (i == 1000)
      return Failure[Int](new Service4Exception)
    Try(i)
  }
  def rollbak()={}
}

