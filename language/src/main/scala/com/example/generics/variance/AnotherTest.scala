package com.example.generics.variance

class A
class B extends A
class C extends A
class D extends B


class Invariant[T]
class Covariant[+T]
class Contravariant[-T]


class I1[T]
class I2[+T]
class I3[-T]

class P
class Q extends P
class R extends Q

class Test1(val i:I1[Q])
class Test2(val i:I2[Q])
class Test3(val i:I3[Q])

object AnotherTest {
  def main(args: Array[String]): Unit = {
    var x1: Invariant[String] = new Invariant[String]
//    var x2: Something[String] = new Something[Int]//will not compile invariant
    var y1:Covariant[A] = new Covariant[A]
    var y2:Covariant[A] = new Covariant[B]//compiles covariant

    var z1:Contravariant[B] = new Contravariant[B]
//    var z2:Contravariant[B] = new Contravariant[D]//will not compile.
    var z2:Contravariant[B] = new Contravariant[A]//will compile contravariant


    val p = new P
    val q = new Q
    val r = new R

    new Test1(new I1[Q])
//    new Test1(new I1[R])//will not compile since I is invariant
//    new Test1(new I1[P])//will not compile since I is invariant

//    new Test2(new I2[P]) //will not compile since its not contravariant. cannot accept a supertype
    new Test2(new I2[Q]) //ok
    new Test2(new I2[R]) //ok

    new Test3(new I3[P]) //ok contravariant, accepts a supertype
    new Test3(new I3[Q]) //ok accepts same type
//    new Test3(new I3[R]) // fail, not a covariant will not accept a subtype of Q
  }
}
