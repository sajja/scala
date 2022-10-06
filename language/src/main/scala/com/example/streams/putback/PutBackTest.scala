package com.example.streams.putback

import org.apache.commons.codec.binary.Base32InputStream

import java.io.{ByteArrayInputStream, InputStream, PushbackInputStream}

object PutBackTest {
  def pushBackStream2(is: InputStream) = {
    println(s"\npushback bytes")
    val pattern = Seq(115, 111)
    val pbs = new PushbackInputStream(is)
    var bom: Array[Byte] = new Array[Byte](2)
    pbs.read(bom)
    if (pattern(0) == bom(0) && pattern(1) == bom(1)) {
      //match
    } else {
      pbs.unread(bom)
    }
    inputStreamTest(pbs)
  }

  def pushBackStream1(is: InputStream) = {
    val pattern = Seq(115, 111, 109)
    println(s"\nDrop bytes")
    Iterator.continually(is.read()).dropWhile(pattern.contains(_)).takeWhile(_ != -1).foreach(print)
  }

  def inputStreamTest(is: InputStream) = {
    Iterator.continually(is.read()).takeWhile(_ != -1).foreach(print)
  }

  def main(args: Array[String]): Unit = {
    val original = "some data".getBytes()
    inputStreamTest(new ByteArrayInputStream(original))
    pushBackStream1(new ByteArrayInputStream(original))
    pushBackStream2(new ByteArrayInputStream(original))

  }
}
