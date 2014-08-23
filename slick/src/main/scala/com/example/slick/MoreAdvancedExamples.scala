package com.example.slick

import com.example.slick.util.DatabaseWrapper
import com.example.slick.util.domain.{Ability, Domain, Player}

import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by sajith on 8/23/14.
 */
object MoreAdvancedExamples extends Domain {

  implicit val session = DatabaseWrapper.session

  def bootstrap() = {
    try {
      abilities.ddl.drop
      players.ddl.drop
    } catch {
      case e: Exception => e.printStackTrace()
    }

    try {
      players.ddl.create
      abilities.ddl.create
    } catch {
      case e: Exception => e.printStackTrace()
    }

    players.insertAll(
      Player("Ice"),
      Player("Rasthafarean"),
      Player("Cr^zyHors3")
    )

    abilities.insertAll(
      Ability("Breath fire", 1),
      Ability("Invisibility", 1)
    )
  }


  def precompiledFindById() = {
    for {
      id <- Parameters[Int]
      p <- players if p.id is id
    } yield p
  }
  def main(args: Array[String]) {
    bootstrap()
    println("\t------- Returns the pk of the inserted item ---------")
    println(insertAndReturnPKey.insert(Player("Blayde")))
    println("\n")

    println("\t------- Same with unreadable generics ---------")
    println(genericInsAndRetKey[Player, Players, TableQuery[Players]](TableQuery[Players]).insert(Player("")))
    println("\n")

    println("\t------- Inner Join example 1 (flat map) ---------")
    val flatMapJoin = players flatMap { p =>
      abilities filter (ab => p.id === ab.playerId) map { xx =>
        (p.name, xx.name)
      }
    }
    println(flatMapJoin.selectStatement)
    flatMapJoin.list().foreach(println)
    println("\n")

    println("\t------- Inner Join example 2 (slick innerjoin) ---------")
    val x = players innerJoin abilities on (_.id === _.playerId)
    val explictInnerJoin = for {
      (p, a) <- players innerJoin abilities on (_.id === _.playerId)
    } yield (p.name, a.name)
    println(x.selectStatement)
    explictInnerJoin.foreach(println)
    println("\n")

    println("\t------- Precompiled queries ---------")
    val preCompiledFindById = for {
        id <- Parameters[Int]
        p <- players if p.id is id
    } yield p
//
//    z(1).foreach(println)
    precompiledFindById()(2).foreach(println)
    val findby = players.findBy(_.id)
    findby(2).foreach(println)




    val s = System.nanoTime()
    for (i <-1 to 100000) {
      findby(2).foreach(_=>{})
    }
    val e = System.nanoTime()
    val nor = e-s

    val start = System.nanoTime()
    for (i <-1 to 100000) {
      preCompiledFindById(2).foreach(_=>{})
    }

    val end = System.nanoTime()
    val pre = end-start
    println("Total time precompiled " + pre)

    println("Total time normal" + nor)
    println("Diff " + (pre-nor))
    session.close()
  }
}