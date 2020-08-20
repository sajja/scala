package com.example.di.constructor

trait Service2 {
  def doService2()

}

class Service2Impl() extends Service2 {
  val service3: Service3 = ???

  override def doService2(): Unit = println("Do service2")
}
