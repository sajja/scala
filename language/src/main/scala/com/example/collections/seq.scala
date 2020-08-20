package com.example.collections

object SeqTest {
  def printSeq[T](s: Seq[T]) = {
    println("\n------")
    s.foreach(print)
    println("\n------")
  }


  def seqBasicTest1() = {
    val seq = Seq(1, 2, 3, 4, 5, 6, 7, 8)
    printSeq[Int](seq)
    printSeq[Int](seq ++ Seq(9, 10))
    printSeq[Int](seq.+:(0))
    printSeq[Int](seq :+ 9)
    printSeq(seq.collect {
      case a: Int if a > 5 => a + 100
    })
    printSeq(seq.filter((x: Int) => x > 5))
    printSeq(seq.filter(_ > 5))

  }


  def main(args: Array[String]) {
    seqBasicTest1()
  }
}