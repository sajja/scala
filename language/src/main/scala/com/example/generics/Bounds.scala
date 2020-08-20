package com.example.generics

class A
class B extends A
class C extends A
class D extends B
class E extends D

class X1[T<:B]
class X2[T>:B]
class X3[T<:B]

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

//    new X1[A]//fail A is not subtype of B
      new X1[B]//ok
      new X1[D]//ok D is subtype of B

      new X2[A]//ok A is supetype of B
      new X2[B]//ok B=B
//      new X2[D]// fail D is subtype of B

  }
}
