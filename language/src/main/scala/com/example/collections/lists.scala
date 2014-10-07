package com.example.collections

/**
 * Created by sajith on 8/4/14.
 */

class MyList(val list: List[String]) {

  def this() = {
    this(Nil)
  }

  def push(item: String) = {
    val dupsRemoved = list.filterNot(i => i.equals(item))
    new MyList(item :: dupsRemoved)
  }

  def print() = {
    list.foreach(println)
  }
}

object lists {
  def main(args: Array[String]) {
    new MyList().push("hello").push("world").push("hello").print()
  }
}
