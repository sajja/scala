package com.example.scratchpad

import com.example.scratchpad.Crypto.cryptOp

import scala.sys.process.Process
import scala.util.Try


object Crypto {
  type cryptOp = (Array[Byte], Map[String, Key]) => Try[Array[Byte]]
}

class Key(val key: String) {
  override def toString: String = key
}

object Key {

  def apply(key: String) = new Key(key)
}

object Encrypt {
  def apply[A](algo: String): cryptOp = {
    if (algo == "RSA") (payload: Array[Byte], settings: Map[String, Key]) => {
      Try {
        val x = "Encrypting " + new String(payload) + " with algo " + algo + " with settings " + settings.mkString(",")
        println(x)
        ("encrypted "  + new String(payload)).getBytes
      }
    } else { (payload: Array[Byte], setting: Map[String, Key]) => Try("".getBytes()) }
  }
}

object Decrypt {
  def apply[A](algo: String): cryptOp = {
    if (algo == "RSA") (payload: Array[Byte], settings: Map[String, Key]) => {
      Try {
        val x = "Decrypting "  + new String(payload) + " with algo " + algo + " with settings " + settings.mkString(",")
        println(x)
        ("decrypted "  + new String(payload)).getBytes
      }
    } else { (payload: Array[Byte], setting: Map[String, Key]) => Try("".getBytes()) }
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    val x = List(Map("1"->1,"2"->2,"3"->3),Map("1"->1,"2"->2,"3"->3),Map("1"->1,"2"->2,"3"->3))
    println(for (i<-x) yield i.foldLeft(" ")((s: String, tuple: (String, Int)) => s + tuple._1 + tuple._2))
  }
}


