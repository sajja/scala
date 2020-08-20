package com.example.generics

class Base1
class Der extends Base1
object BasicGenericFunction {



  def f1[A](a:A) = {
    println(a)
  }


  def f2(a:Base1)={println(a)}
  def f3[A](a:A)={println(a)}


  def main(args: Array[String]) {
    f1("hello")
    f1(111)
    f1(111.22)
    f1(this)
//    f1[Int]("") wont compile
    f2(new Der)
    f3[Base1](new Der)
  }
}