package com.example.generics

/**
 * Created by sajith on 8/23/14.
 */

abstract class Wrapped

class Wrapper[T <: Wrapped] {
  def doSome(t: T) = println(t)
}

class W1 extends Wrapped

class W2 extends Wrapped


object MoreTests {

  def genericDoSome(w: Wrapper) = {

  }

  def main(args: Array[String]) {
    new Wrapper[W1]().doSome(new W1)
    new Wrapper[W2]().doSome(new W2)
  }
}
