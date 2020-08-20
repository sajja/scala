package com.example.di.constructor

import java.io._

object TestConstructor {
  def main(args: Array[String]): Unit = {
    val file = new File("/home/sajith/r.csv")
    val filterFile = new File("/home/sajith/filter_more.csv")
    val br = new BufferedReader(new FileReader(file))
    val write = new BufferedWriter(new FileWriter(filterFile))
    var line = ""
    var second = ""
    var count = 0
    var endpoint = ""
    var paysol = 0
    var polling = 0
    var payment = 0


    while (line != null) {
      line = br.readLine()
      if (line != null) {
        val data = line.split(" ")
        val time = data(0)
        val ep = data(1)

        val seconds = time.split(",")
        if (seconds(0) == second) {

          if (ep.contains("paysol"))
            paysol = paysol + 1
          else if (ep.contains("polling"))
            polling = polling + 1
          else
            payment = payment + 1
        } else {

          val data = s"${second},$polling,$paysol,$payment\n"
          write.write(data)
          println(data)
          paysol = 0
          polling = 0
          payment = 0
          second = seconds(0)
        }
      }
    }

    br.close()
    write.close()

  }
}
