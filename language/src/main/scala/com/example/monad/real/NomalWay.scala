package com.example.monad.real

import java.io.IOException

import com.sun.media.sound.InvalidDataException

class Data(var prop1: Int, val prop2: Int, val endpointUri: String) {
}

class Response(val code: Int, val payload: Int)

class Sender1 {
  def send(data: Data): Unit = {
    try {
      if (data != null) {
        val extractedData = new Extractor1().extract(data)
        val response = new EnpointCaller11().callEndpointUri(extractedData._3, extractedData._1)
        new ResponseHandler1().handleResponse(response)
      }
    } catch {
      case e: Throwable => println(e.printStackTrace())
    }
  }
}

class EnpointCaller11 {
  @throws(classOf[IOException])
  def callEndpointUri(uri: String, data: Int): Response = {
    println("Calling endpoint .....")
    if (data == 0) {
      null
    } else if (data == 200) {
      throw new IOException("Error calling endpoint")
    } else {
      new Response(200, 42)
    }
  }

}

class Extractor1 {
  def extract(data: Data) = {
    if (data == null) {
      throw new InvalidDataException("invalid data")
    }

    println("extracting data.....")
    (data.prop1, data.prop2, data.endpointUri)
  }
}

class ResponseHandler1 {
  def handleResponse(r: Response) = {
    println("Hanindg response ...." + r.payload)
  }
}

object Test {
  def main(args: Array[String]) {
    new Sender1().send(new Data(1, 2, "ddd"))
  }
}
