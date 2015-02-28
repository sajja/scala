package com.example.statemutation

class MutatedNextInt(var seed:Int) {
  def nextInt = {
    seed +=1
    seed
  }
}


class ImmutableNextInt(val seed:Int) {
  def nextInt=new ImmutableNextInt(seed + 1)
}

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  def simple(seed: Long): RNG = new RNG {
    def nextInt = {
      val seed2 = (seed*0x5DEECE66DL + 0xBL) &
        ((1L << 48) - 1)
      ((seed2 >>> 16).asInstanceOf[Int],
        simple(seed2))
    }
  }
}


object Test {

  def main(args: Array[String]) {
    val mutatedNextInt = new MutatedNextInt(1)
    println(mutatedNextInt.nextInt)
    println(mutatedNextInt.nextInt)
    println(mutatedNextInt.nextInt)

    val iNI = new ImmutableNextInt(1)
    println(iNI.nextInt.seed)
    println(iNI.nextInt.nextInt.seed)


    println(RNG.simple(1).nextInt)
  }
}
