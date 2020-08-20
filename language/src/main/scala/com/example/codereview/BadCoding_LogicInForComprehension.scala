package com.example.codereview

class BadCoding_LogicInForComprehension {
  def exampleBad(i: Int, j: String) = {
    for {
      op1 <- if (i == 10) {
        Some(i * 10)
      } else if (i == 100) {
        Some(i * 20)
      } else if (i == -1) {
        Some(i * 30)
      } else {
        None
      }
      op2 <- if (j == "1") {
        Some(1)
      } else if (j == "2") {
        Some(2)
      } else {
        None
      }
    } yield {
      (op1, op2)
    }
  }

  def exampleRefactor(i: Int, j: String) = {
    for {
      op1 <- mapIntToInt(i)
      op2 <- mapStringToInt(j)
    } yield {
      (op1, op2)
    }
  }

  def mapStringToInt(j: String) = {
    if (j == "1") {
      Some(1)
    } else if (j == "2") {
      Some(2)
    } else {
      None
    }
  }

  def mapIntToInt(i: Int) = {
    if (i == 10) {
      Some(i * 10)
    } else if (i == 100) {
      Some(i * 20)
    } else if (i == -1) {
      Some(i * 30)
    } else {
      None
    }
  }
}
