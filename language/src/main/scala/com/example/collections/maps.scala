package com.example.collections

/**
 * Created by sajith on 8/4/14.
 */
object maps {

  def printMap[A, B](map: Map[A, B]) = {
    map.foreach(println)
  }

  def group_by_test() {
    val votes = Seq(("Scala", 10), ("Java", 4), ("C", 10), ("C++", 15), ("Python", 8), ("Scala", 12), ("Python", 1), ("Java", 1))
    SeqTest.printSeq(votes)

    //Group by language.
    val byLang_BadWay = votes groupBy ((tup: (String, Int)) => tup._1)

    val byLang_BetterWay = votes groupBy {
      case (lang, _) => lang
    }

    val sumByLangBestWay = byLang_BetterWay map { case (lang, counts) =>
      val countsOnly = counts map { case (_, count) => count }
      (lang, countsOnly.sum)
    }

    printMap(sumByLangBestWay)

    //same as sumByLang
    val sumByLang2 = byLang_BetterWay.map((a: ((String, Seq[(String, Int)]))) => {
      val lang = a._1
      val seq = a._2
      (lang, seq.map( (f:((String,Int)))=>f._2).sum)
    })

    printMap(sumByLang2)


    //same as sumByLang, sum is just a reduce function.
    val sumByLang3 = byLang_BetterWay.map((a: ((String, Seq[(String, Int)]))) => {
      val lang = a._1
      val seq = a._2
      (lang, seq.map( (f:((String,Int)))=>f._2).reduce((i:Int,j:Int)=>i+j))
    })

    printMap(sumByLang3)


      println("X")
  }

  def map_test() {
    val seq = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    SeqTest.printSeq(seq)

    val by2times = seq.map((i: Int) => i * 2)
    SeqTest.printSeq(by2times)

    val sameThing = seq.map(_ * 2)
    SeqTest.printSeq(sameThing)

  }

  def main(args: Array[String]) {
    group_by_test()
    map_test()
  }
}
