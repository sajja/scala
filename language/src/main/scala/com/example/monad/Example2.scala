package com.example.monad

object Example2 {

  def main(args: Array[String]) {
    val x = Seq(1,2,3,4)
    val y = for {
      i <-x
    } yield i

    print(y)

    val y1 = x.flatMap((i: Int) => List(i))
    print(y1)
  }
}
