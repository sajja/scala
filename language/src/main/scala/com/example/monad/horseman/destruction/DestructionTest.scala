package com.example.monad.horseman.destruction

import scala.concurrent.Future

/**
  * Created by sajith on 3/11/16.
  */

trait Type[A] {
  def flatMap[B](f: A => Type[B]): Type[B]

  def map[B](f: A => B): Type[B]

  def unit(a: A): Type[A]
}

trait MIO[A] {
  self =>
  def map[B](g: A => B) = new MIO[B] {
    override def run: B = g(self.run)
  }

  def flatMap[B](g: A => MIO[B]): MIO[B] = new MIO[B] {
    override def run: B = g(self.run).run
  }

  def run: A
}

object DestructionTest extends App {

  def sideEffect() = {
    print("Enter your name >> ")
    val name = readLine()
    print(s"\nHello $name")
  }

  def printMsg(str: String) = new MIO[Unit] {
    override def run = println(s"$str")
  }

  def read = new MIO[String] {
    override def run: String = readLine()
  }

  //  val v = for {
  //    x <- read
  //    y <- print(x)
  //  } yield x


  val io = for {
    _ <- printMsg("Enter your name >>")
    name <- read
    _ <- printMsg(s"Welcome $name")
  } yield {}

  io.run

}
