package com.example.zio

import zio.ZIO.console
import zio.{Console, Random, Schedule, ZIO}

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
    val j = i.flatMap((str: String) => ZIO.fromTry(Try(Integer.parseInt(str)))).catchAll(x => ZIO.fail(new IOException("dasfasjfdafafdafaf")))
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
      val x = ZIO.attemptBlocking(file.getLines().mkString("\n"))
      x
    }
  }

  def fio2: ZIO[Any, Throwable, Unit] = {
    val i = ZIO.fromTry(Try {
      100
    })
    val j = Console.readLine.map((str: String) => 42)
    val xx = (i: Int) => ZIO.succeed(println("Closing the file......" + i))
    ZIO.acquireReleaseWith(j)(xx) {
      f => Console.printLine("Running content " + f)
    }
  }


  def combiningEffects1() = {
    Console.readLine.zipWith(Console.readLine)((a, b) => a + ":" + b).flatMap(Console.print(_))
    //    i.flatMap(_ => Console.printLine("DDD"))
  }

  def combiningEffects2() = {
    val i = ZIO.succeed(100)
    val j = ZIO.succeed(200)
    i.zipRight(j).map(println)

  }

  def worker(i: Int) = {
    println("calling...")
    if (i == 0) {
      throw new IOException("IOE")
    } else if (i == 1) {
      throw new Exception("Ex")
    } else if (i == 2) {
      throw new NumberFormatException("NFE")
    } else {
      1000
    }
  }

  def catchAll() = {
    ZIO.readFile("/home/sajith/cantread").catchAll(e => Console.printLine(e.getMessage()))
  }

  def catchSome() = {
    val i = Console.printLine("daf")
    ZIO.from(worker(1)).retry(Schedule.recurs(5)).catchSome {
      case e: IOException => Console.printLine(e.getMessage)
      case _ => Console.printLine("xxx")
    }

  }

  override def run = catchSome()

}
