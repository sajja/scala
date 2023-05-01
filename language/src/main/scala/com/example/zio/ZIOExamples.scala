package com.example.zio

import zio.{Console, Random, ZIO}

import java.io.IOException
import scala.io.Source
import scala.util.Try


object ZIOExamples extends zio.ZIOAppDefault {
  def helloWorld: ZIO[Any, IOException, Unit] = {
    for {
      line <- Console.readLine
      _ <- Console.printLine(line)
    } yield {}
  }

  def example = {
    val s1 = ZIO.succeed(42)
    val s2 = ZIO.fail(new IOException("dafdas"))
    val s3 = ZIO.fail("Dalkfjlaksfdjlk")
    s1
  }

  def optionTest = {
    //    val opt = Some(42)
    val opt = Some(43)
    val i = None
    val zopt = ZIO.fromOption(i).catchAll(x => ZIO.fromOption(Some(1)))
    val j = for {
      x <- zopt
      _ <- Console.printLine(x)
    } yield ()
    j
  }

  def errorHand1 = {
    val i = Console.readLine
    val j = i.flatMap((str: String) => ZIO.fromTry(Try(Integer.parseInt(str)))).catchAll(x=>ZIO.fail(new IOException("dasfasjfdafafdafaf")))
    for {
      x <- j
      _ <- Console.printLine(x)
    } yield ()
  }

  def tryTest = {
    val tt = ZIO.fromTry(Try {
      1 / 0
    })

  }

  def fio = {
    ZIO.acquireReleaseWith(
      ZIO.attemptBlocking(Source.fromFile("/home/sajith/tmp/hello"))
    )(file => ZIO.attempt(file.close()).orDie) { file =>
      ZIO.attemptBlocking(file.getLines().mkString("\n"))
    }
  }

  override def run = errorHand1


}
