package com.example.highertypes

/**
  * Created by sajith on 2/28/16.
  */
class Cont[F[_]] {
  def vvv[T](x: F[T]) = {
    println(x)
  }
}

class Def[_] {

}

object HigherTypesTest {
  def main(args: Array[String]) {
    val cont2 = new Cont[List]
    cont2.vvv(List("testo"))
    cont2.vvv(List(1,2,43))
  }
}
