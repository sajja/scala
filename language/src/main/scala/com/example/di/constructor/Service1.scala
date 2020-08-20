package com.example.di.constructor

trait Service1 {
  def doService1()

}
class Service1Impl(service2:Service2) extends Service1 {

  override def doService1(): Unit = println("doService1()")
}
