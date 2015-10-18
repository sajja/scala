package com.example.cake.modules

trait Thing[A,B] {
  type X = Int
  def source(a:A):X
}

object Test1 {

  def main(args: Array[String]) {
    def test(t:Thing[Int,String]) ={
      val x:t.X = t.source(100)
    }

    test(new Thing[Int, String] {
      override def source(a: Int): X = 1
      override type X = Int
    })
  }
}
