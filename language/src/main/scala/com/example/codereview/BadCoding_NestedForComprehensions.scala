package com.example.codereview

class BadCoding_NestedForComprehensions {
  def exampleBad(i: Int) = {
    for {
      a <- for {
        inner1 <- Some(i)
        inner2 <- Some(i * 100)
        inner3 <- for {
          ii <- Some(inner1 * inner2)
        } yield ii
      } yield {
        inner1 * inner2
      }
    } yield a
  }

  def exampleRefactor(i: Int) = {
    for {
      a <- yield1(i)
    } yield a
  }

  def yield1(i: Int) = {
    for {
      inner1 <- Some(i)
      inner2 <- Some(i * 100)
      inner3 <- yield2(inner1, inner2)
    } yield {
      inner1 * inner2
    }
  }

  def yield2(i: Int, j: Int) = {
    for {
      ii <- Some(i * j)
    } yield ii

  }
}
