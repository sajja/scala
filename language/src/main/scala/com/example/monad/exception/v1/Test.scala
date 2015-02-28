package com.example.monad.exception.v1

import com.example.monad.exception.{Service4Exception, Service3Exception, Service2Exception, Service1Exception}

object Service1 {
  def invoke(i:Int) = {
    if (i == 0)
      throw new Service1Exception
    i
  }
  def rollbak()={}
}

object Service2 {
  def invoke(i:Int) = {
    if (i == 10)
      throw new Service2Exception
    i
  }
  def rollbak()={}
}

object Service3 {
  def invoke(i:Int) = {
    if (i == 100)
      throw new Service3Exception
    i
  }
  def rollbak()={}
}

object Service4 {
  def invoke(i:Int) = {
    if (i == 1000)
      throw new Service4Exception
    i
  }
  def rollbak()={}
}

