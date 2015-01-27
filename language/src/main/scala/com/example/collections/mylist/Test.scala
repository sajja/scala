package com.example.collections.mylist

class MyList[T](val i:List[T]) {
  def add(item:T) = new MyList(item :: this.i)
}
object Test {
  def main(args: Array[String]) {
    var i = new MyList[Int](List(1,2,3))
    i = i.add(100)
    i.i.foreach(println)
  }

}
