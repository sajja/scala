package com.example.comprehension

object Test1 {

  def printAll1(from: Int, end: Int) = {
    for (i <- from to end) {
      println(i)
    }
  }

  def printAll[T<:Seq[Int]](list: Seq[Int]) = {
    list.foreach(println)
  }

  def pA[T](list:IndexedSeq[T]) = {
    list.foreach(println)
  }
  def printOdd(from: Int, end: Int) = {
    for (i <- from to end if i % 2 != 0) yield i
  }


  def main(args: Array[String]) {
    printAll1(1, 3)
    printAll(printOdd(1, 10))
    pA(printOdd(1, 10))
  }
}
