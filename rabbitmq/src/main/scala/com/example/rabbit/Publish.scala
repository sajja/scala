package com.example.rabbit
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object Publish {
  def main(args: Array[String]): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("192.168.1.12")
//    factory.setHost("localhost")
    factory.setUsername("pagero")
    factory.setPassword("Pagdia1")
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    val queueName = "testqueue"
    channel.queueDeclare(queueName, false, false, false, null)

    while(true) {
      channel.basicPublish("", queueName, null, "xadfa".getBytes())
      Thread.sleep(1000)
    }

  }
}
