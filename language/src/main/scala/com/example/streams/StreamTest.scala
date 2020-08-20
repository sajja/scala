package com.example.streams

import scala.util.Random

/**
  * Created by sajith on 1/6/16.
  */
object StreamTest {
  def make: Stream[Int] = Stream.cons(util.Random.nextInt(10), make)

  def main(args: Array[String]) {
    println(make.head)
  }
}



