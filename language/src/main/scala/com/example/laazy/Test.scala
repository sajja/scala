package com.example.laazy


object Test {
 def evenNumbersGoodWay(i:Int):Stream[Int] = {
   def loop(i:Int, list:Stream[Int]):Stream[Int]={
     if (i == 100000)
       return list

     i % 2 match {
       case 0 => loop(i+1,i#::list)
       case _ => loop(i+1,list)
     }
   }

   loop(i,Stream[Int]())
 }

 def evenNumbersBadWay(i:Int):List[Int] = {
   def loop(i:Int, list:List[Int]):List[Int]={
     if (i == 100000)
       return list

     i % 2 match {
       case 0 => loop(i+1,i::list)
       case _ => loop(i+1,list)
     }
   }

   loop(i,List[Int]())
 }

  def main(args: Array[String]): Unit = {
    println(evenNumbersBadWay(1))
    val top10 = evenNumbersGoodWay(1) take 10
    println(top10.toList)
  }
}
