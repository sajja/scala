package com.example.monoid

import scala.util.Success

trait Monoid[T] {
  def op(a: T, b: T): T
  def zero: T
}

class OrderLine(val name:String,val price:Int,val qtty:Int = 0)



object Test {
  val olm = new Monoid[OrderLine] {
    override def op(a: OrderLine, b: OrderLine): OrderLine = {new OrderLine("Total", a.price+b.price)}

    override def zero: OrderLine = new OrderLine("Total",0)
  }

  val optionAdd = new Monoid[Option[String]] {
    override def op(a: Option[String], b: Option[String]): Option[String] = {
      for{
        x1<-a
        x2<-b
      } yield x1+x2
    }

    override def zero: Option[String] = None
  }


  def getSome: Option[String] = {
    Some[String]("A")
  }

  def getNone: Option[String] = {
    None
  }

  def print(a: String) = {
    println(a)
  }

  def main(args: Array[String]) {
    val stringMonoid = new Monoid[String] {
      override def op(a: String, b: String): String = a + "|" + b

      override def zero: String = ""
    }


    println(stringMonoid.op("Hello", "World"))
    println(stringMonoid.op("Hello", stringMonoid.zero))

    val intMultiply = new Monoid[Int] {
      override def op(a: Int, b: Int): Int = a * b

      override def zero: Int = 1
    }

    println(intMultiply.op(100, 10))

    //    val x = getSome
    val x = getNone
    val y = getSome
    //    val y = None
    val z = for {
      a <- x
      b <- y
    } yield a + b

    val xxx = x.flatMap((s1: String) => y.flatMap((s2: String) => new Some[String](s1 + s2)))
    println("XXXX" + z)
    println("XXXX" + xxx)

    val words = List("A_", "B_", "C_", "D_", "E_")
    println(words.foldRight(stringMonoid.zero)(stringMonoid.op))


    println(optionAdd.op(Some("hello"),Some("world")))
    println(optionAdd.op(None,Some("world")))

    val items = List(new OrderLine("beer",10),new OrderLine("Red",100), new OrderLine("Burbone",100), new OrderLine("Gold",1000))

    val total = items.reduce((value: OrderLine, value0: OrderLine) => new OrderLine("Total", value.price+value0.price))
    val total1 = items.foldRight(olm.zero)( olm.op)


    println("Total " + total.price)
    println("Total " + total1.price)
  }
}
