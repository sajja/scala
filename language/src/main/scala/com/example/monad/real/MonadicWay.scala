package com.example.monad.real

import com.example.monad.validation.{Success, Validation}


class Sender {
  def send(data: Data): Unit = {
    for {
      extractedData <- new Extractor().extract(data)
      response <- new EnpointCaller().callEndpointUri(extractedData._3, extractedData._1)
      validationResult <- new ResponseHandler().handleResponse(response._1, response._2, response._1)
    } yield validationResult
  }
}

class EnpointCaller {
  def callEndpointUri(uri: String, data: Int): Validation[Exception, (Int, String, Int)] = {
    println("Calling end point.....")
    new Success[Exception, (Int, String, Int)](200, "success", 42)
  }
}

class Extractor {
  def extract(data: Data): Validation[Exception, (Int, Int, String)] = {
    println("Extracting data.....")
    new Success[Exception, (Int, Int, String)]((data.prop1, data.prop2, data.endpointUri))
  }
}

class ResponseHandler {
  def handleResponse(resultCode: Int, description: String, payload: Int): Validation[Exception, Int] = {
    println("Handling response ......")
    new Success[Exception, Int](0)
  }
}


object MonadicWay {

  def main(args: Array[String]) {

  }

}
