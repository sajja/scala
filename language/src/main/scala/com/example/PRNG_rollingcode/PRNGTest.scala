package com.example.PRNG_rollingcode

import java.util.Random

object Rx {
  var lastPrng: Int = 1

  def check(i: Int) = {
    val rng = new Random(lastPrng)
    lastPrng = rng.nextInt()
    if (lastPrng == i) {
      println("Code matches")
    } else {
      var j = 0
      while (lastPrng != i && j <= 500) {
        lastPrng = new Random(lastPrng).nextInt()
        j = j + 1
      }

      if (j > 500) {
        println("Reset")
        lastPrng = 1
      } else {
        println(s"Resynced...$j")
      }
    }
  }
}

object Tx {
  var currentPrng: Int = 1

  def emit(): Int = {
    val rng = new Random(currentPrng)
    currentPrng = rng.nextInt()
    currentPrng
  }
}


object PRNGTest {
  def txClickedMultipleTimes() = {
    var code = Tx.emit()
    code = Tx.emit()
    code = Tx.emit()
    code = Tx.emit()
    code = Tx.emit()
    code = Tx.emit()
    Rx.check(code) //not valid, but resynced
    code = Tx.emit()
    Rx.check(code) //now works
  }

  def TXandRXinSync() = {
    var code = Tx.emit()
    Rx.check(code)
  }

  def rxOutOfSync() = {
    Rx.check(10)
  }

  def main(args: Array[String]): Unit = {
//    txClickedMultipleTimes()
    TXandRXinSync()
    rxOutOfSync()
    TXandRXinSync()
    TXandRXinSync()
  }
}
