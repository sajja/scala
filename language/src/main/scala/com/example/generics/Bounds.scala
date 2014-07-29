package com.example.generics
class Base
class MyType extends Base
class SubType extends MyType

class AnotherType[B <: MyType] {
}

class YetAnotherTest[P] {
  def takeThis[Q<:P](q:Q) = println(q)
}

object BoundsTest {
  def upperBound[A<: SubType](a:A) {
    println(a)
  }
  def lowerBound[A >: MyType](a:A) {
    println(a)
  }

  def main(args: Array[String]) {
//    val sT1 = new AnotherType[Int]//fails to compile
    val sT2 = new AnotherType[SubType]
//    fn1[Int](100) //fail to compile
    upperBound[SubType](new SubType)
//    fn1[MyType](new MyType)//fail to compile
    lowerBound  [MyType](new MyType)
//    upperBound[SubType](new SubType)//fail to compile
    lowerBound[Base](new Base)

    val yNT = new YetAnotherTest[MyType]
    yNT.takeThis[SubType](new SubType)
//    yNT.takeThis[Base](new Base)//will not compile
  }
}
