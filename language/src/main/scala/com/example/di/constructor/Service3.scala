package com.example.di.constructor

trait Service3 {
  def doService3()

}

class Service3Impl extends Service3 {
  val service4: Service4 = ???

  override def doService3(): Unit = println("Service3")
}