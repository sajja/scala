package com.example.generics

//Non variant
abstract class Stack[A] {
  def push(e: A) = new NonEmptyStack[A](e,this)

  def pop(): Stack[A]
  def top:A
}

class EmptyStack[A] extends Stack[A] {
  override def top: A = sys.error("error")

  override def pop(): Stack[A] = sys.error("error")
}

class NonEmptyStack[A](e:A,rest:Stack[A]) extends Stack[A] {
  override def top: A = e

  override def pop(): Stack[A] = rest
}

object Test {
  def main(args: Array[String]) {
    val stringStack = new NonEmptyStack[String]("A",new NonEmptyStack[String]("B",new EmptyStack[String]))
    println(stringStack.top)
    println(stringStack.pop().top)
    val intStack = new NonEmptyStack[Int](1,new NonEmptyStack[Int](2,new EmptyStack[Int]))
    println(intStack.top)
    println(intStack.pop().top)
  }
}