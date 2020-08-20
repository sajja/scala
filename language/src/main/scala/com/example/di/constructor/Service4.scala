package com.example.di.constructor

trait Service4 {

  def doService4()
}

class Service4Impl extends Service4 {
  override def doService4(): Unit = println("do service4")
}
