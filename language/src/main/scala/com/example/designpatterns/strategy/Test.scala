package com.example.designpatterns.strategy

/**
 * Created by sajith on 10/11/15.
 */

trait Strategy {
  def alog(i: Int): Int
}

class TestStrategy {
  var s: Strategy = null

  def op(i: Int) = s.alog(i)
}

class Algo1 extends Strategy {
  override def alog(i: Int): Int = {
    println("xxxxxxx")
    println("zzzz")
    println("ppp")
    println("wwwwwj")
    println("qqqq")
    i * 100
  }
}

object FnFact {
  def apply(i:Int) = {
    i match {
      case 1 => (x:Int)  => x*i
      case 2 => (x:Int)  => x*i
      case 3 => (x:Int)  => x*i
      case _ => (
        
        )
    }
  }
}

object Test {
  def strategyFn(i:Int, fn: Int=>Int) = {
    fn(i)
  }
  def main(args: Array[String]) {
    val st = new TestStrategy
    st.s = new Algo1
    st.op(100)
  }
}
