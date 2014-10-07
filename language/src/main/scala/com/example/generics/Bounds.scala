package com.example.generics

class A
class B extends A
class C extends A
class D extends B
class E extends D
object BoundsTest {
  def upperBound[T<:B](t:T) = {
    println(t)
  }

  def lowerBound[T>:B](t:T) = {
    println(t)
  }

  def main(args: Array[String]) {
    //upperboud -> T should be subtype of give type
//    upperBound[A](new A)//fail. A is not sub type of B
    upperBound[B](new B)//ok same type
//    upperBound[C](new C)//fail C is not subtype of B
    upperBound[D](new D)//ok D is subtype of B

    //lowerbound -> T should be supertype of given type

    lowerBound[A](new A)//ok. A is a supertype of B
    lowerBound[B](new B)//ok. same type
//    lowerBound[C](new C)//fail. C is not subtype of b
//    lowerBound[D](new D)//fail. D is not supertype of B
  }
}
