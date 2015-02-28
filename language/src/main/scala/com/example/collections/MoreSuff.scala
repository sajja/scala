package com.example.collections

object MoreSuff {

  def forYield() = {
    val x = Seq[Int](1, 2, 3, 4, 5, 6, 7)
    val j = for {
      i<-x
    } yield i+100
    j.foreach(println)

    x.map(_+1)
  }

  def foldTest(start:Int) = {
    val x = Seq[Int](1, 2, 3, 4, 5, 6, 7)
    println("Fold " + x.fold(start) {
      (z, i) => z + i
    })
  }

  def foldTest2(start:Int) = {
    val x = Seq[Int](1, 2, 3, 4, 5, 6, 7)
    println("L" + x.foldLeft(start) {
      (z, i) => z - i
    })
    val r = Seq[Int](1, 2, 3, 4, 5, 6, 7)
    println("R " + x.foldRight(start) {
      (z, i) => z - i
    })
  }


  def reduceTest() = {
    val x = Seq[Int](1, 2, 3, 4, 5, 6, 7)

    println("Reduce " + x.reduce(_ + _))
    println("Reduce Left " + x.reduceLeft((value: Int, i: Int) => {
      println("Reducing " + value + " " + i)
      value + i
    }))

    println("Reduce Right " + x.reduceRight((value: Int, i: Int) => {
      println("Reducing " + value + " " + i)
      value + i
    }))

  }


  def main(args: Array[String]) {

    reduceTest()
    println("Fold test ---------------")
    foldTest(0)
    foldTest(100)
    println("End Fold test ---------------")
    forYield()
    foldTest2(0)
  }

}
