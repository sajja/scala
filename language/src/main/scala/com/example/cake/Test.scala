package com.example.cake


trait FooAble {
  def foo(): String
}

trait BazAble {
  def baz() = "baz too"
}


trait MyFooAble extends FooAble {
  def foo() = "foo impl"
}


class BarUsingFooAble {
  this: FooAble with BazAble =>
  //see note #1
  def bar() = "bar calls foo: " + foo()+ "with baz " + baz()   //see note #2
}


object Main {
  def main(args: Array[String]): Unit = {
  }
}