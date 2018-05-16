package com.example.rabbit.sstone

import com.github.sstone.amqp.{Amqp, ChannelOwner, ConnectionOwner, Consumer}
import com.rabbitmq.client.ConnectionFactory
import akka.actor._
import com.github.sstone.amqp.Amqp._
import com.rabbitmq.client.AMQP.{Channel, Queue}
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._

object SPublisher {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("sstone")
    val connFactory = new ConnectionFactory()
    connFactory.setHost("dev.localhost")
    connFactory.setPort(5672)
    connFactory.setUsername("pagero")
    connFactory.setPassword("Pagdia1")
    connFactory.setVirtualHost("servicecomm")

    val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))
    val channel = ConnectionOwner.createChildActor(conn, ChannelOwner.props())
    Thread.sleep(3000)
    channel ! DeclareQueue(QueueParameters("my_queue", passive = false, durable = false, exclusive = false, autodelete = true))

        for (i <- 1 to 1000000) {
          channel ! Publish("", "my_queue", "Hello world".getBytes())
          Thread.sleep(1000)
        }


//    implicit val timeout = akka.util.Timeout(2000)
//    while (true) {
//      val Amqp.Ok(_, Some(result: Queue.DeclareOk)) = Await.result(
//        (channel ? DeclareQueue(QueueParameters(name = "my_queue", passive = true))).mapTo[Amqp.Ok],
//        5 seconds
//      )
//      println("there are %d messages in the queue named %s".format(result.getMessageCount, result.getQueue))
//      Thread.sleep(1000)
//    }
  }
}


object SConsumer {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("sstone")
    val connFactory = new ConnectionFactory()
    connFactory.setHost("dev.localhost")
    connFactory.setPort(5672)
    connFactory.setUsername("pagero")
    connFactory.setPassword("Pagdia1")
    connFactory.setVirtualHost("servicecomm")

    val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))
    val channel = ConnectionOwner.createChildActor(conn, ChannelOwner.props())
    Thread.sleep(3000)

    val listener = system.actorOf(Props(new Actor {
      def receive = {
        case Delivery(consumerTag, envelope, properties, body) => {
          println("got a message: " + new String(body))
          Thread.sleep(8000000)
          //          sender ! Ack(envelope.getDeliveryTag)
        }
        case _ => {
          print("uxxxxx")
        }
      }
    }))

    val consumer = ConnectionOwner.createChildActor(conn, Consumer.props(listener, channelParams = None, autoack = false))
    consumer ! QueueBind(queue = "my_queue", exchange = "amq.direct", routing_key = "my_key")
    consumer ! AddQueue(QueueParameters(name = "my_queue", passive = false))
  }
}

