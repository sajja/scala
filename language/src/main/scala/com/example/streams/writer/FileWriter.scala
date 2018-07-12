package com.example.streams.writer

import java.io.{BufferedInputStream, File, FileInputStream, FileOutputStream}

object FileWriter {
  def main(args: Array[String]): Unit = {
    val file = new File("/home/sajith/Downloads/SupernaturalS13E17HDTVx264-SVAmp4 _openload.mp4")
    val bos = new FileOutputStream(new File("/tmp/mystream.mp4"))
    var chunk: Array[Byte] = new Array[Byte](1024)

    val is = new BufferedInputStream(new FileInputStream(file))
    var data = 0

    while (data != -1) {
      data = is.read(chunk)
      bos.write(chunk)
      Thread.sleep(3)
    }

    bos.close()
    is.close()
  }
}
