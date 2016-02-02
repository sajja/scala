package com.example.streams.rabbit

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.rabbitmq.client.{MessageProperties, ConnectionFactory}
import io.scalac.amqp.{Connection, ConnectionSettings, Address}

import scala.util.Random


object EventEmitter extends App{

  private def emitSensorData(sensor: String, data: String, date: Date) = {
    val sensorData = s"${df.format(date)} $sensor $data"
    channel.basicPublish("", "test", MessageProperties.PERSISTENT_TEXT_PLAIN, sensorData.getBytes())
  }

  private def generateRandomData(from: Date, interval: Int) = {
    val cal = Calendar.getInstance()
    cal.setTime(from)
    val rand = new Random
    while (true) {
      emitSensorData("Thermostat", getPositiveRandom(10, false).toString, new Date())
      Thread.sleep(1000)
    }
  }

  private def getPositiveRandom(max: Int, zeroIncluded: Boolean = true): Int = {
    val rand = new Random
    val num = rand.nextInt() % max

    if (!zeroIncluded && num == 0) getPositiveRandom(max, false)
    else if (num < 0)
      num * -1
    else
      num
  }
  def emit() = generateRandomData(new Date(), 1)
  val factory = new ConnectionFactory()
  factory.setHost("dev.localhost")
  factory.setUsername("pagero")
  factory.setPassword("Pagdia1")
  val connection = factory.newConnection()
  val channel = connection.createChannel()
  channel.queueDeclare("test", true, false, false, null)
  val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  emit()
}

object RabbitReactiveStream extends App {
  import scala.concurrent.duration._
  def stream() = {
    implicit val system = ActorSystem("Sys")
    implicit val materializer = ActorMaterializer()
    val address = scala.collection.immutable.Seq[Address](Address("dev.localhost", 5672))
    val config = new ConnectionSettings(address, "/", "pxxxxo", "Pxxdxx1", None, 3 second, true, 333 seconds, None)
    val connection = Connection(config)
    val queue = connection.consume("test")
    Source.fromPublisher(queue).runWith(Sink.foreach(x => println(s"${new String(x.message.body.toArray)} xxx")))
  }

  stream()
}
