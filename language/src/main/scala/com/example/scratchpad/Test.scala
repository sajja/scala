package com.example.scratchpad

/**
 * Created by sajith on 10/7/14.
 */

object Test {
  def x(s:String) = s
  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      // catch Exception to catch null 's'
      case e: Exception => None
    }
  }

  def main(args: Array[String]): Unit = {
    val x = Seq(Some(1), Some(2), None,Some(4))
    x.foreach(println)
  }
}
