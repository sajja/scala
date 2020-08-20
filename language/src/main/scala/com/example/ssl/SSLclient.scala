package com.example.ssl

import java.net.URL

/**
  * Created by sajith on 11/23/17.
  */
object SSLclient {
  def main(args: Array[String]): Unit = {
    val uri = new URL("https://www.google.com")
    val con = uri.openConnection()
    val data = new Array[Byte](4000)
    con.getInputStream.read(data)
    println(new String(data))
  }
}
