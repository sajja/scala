package com.example.akka.worker

import akka.actor._
import akka.routing.RoundRobinRouter

case class Start(workers: Int)

case class RunJob(j: Int)

case class Result(i: Int)

case class Print()

//0,1,2,3,4,5
//0,2,4,6,8,10 = 30
class Worker() extends Actor {
  override def receive: Receive = {
    case RunJob(j) =>
      println("Running job... " + j)
      sender ! Result(j * 2)
      Thread.sleep(10)
  }
}

class JobScheduller(workers: Int, iterations: Int) extends Actor {

  import context._

  def x(state: Int, currentIteration: Int): Receive = {
    case Start(i) =>
      val workerRouter = context.actorOf(Props(new Worker).withRouter(RoundRobinRouter(3)), "worker")
      for (j <- 0 to iterations) workerRouter ! RunJob(j)
    case Result(i) =>
      become(x(state + i, currentIteration + 1))
      if (currentIteration == iterations) self ! Print()
    case Print() => println(s"State $state")


  }

  override def receive: Actor.Receive = x(0, 0)
}

object Akka1 {
  def main(args: Array[String]) {
    val system = ActorSystem("JobSystem")
    val scheduller = system.actorOf(Props(new JobScheduller(2, 5)))
    scheduller ! Start(10)
  }
}
