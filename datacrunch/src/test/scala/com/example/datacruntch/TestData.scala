package com.example.datacruntch

import java.io.File

import com.example.datacruntch.processing.FileSystemModule


trait TestData extends FileSystemModule {
  def prepare(testDir: String)(f: (File => Boolean)): Unit = {
    val testLogLocation = new File("testlogs")

    if (!testLogLocation.exists()) throw new Exception("Cannot locate test logs")
    if (!testLogLocation.isDirectory) throw new Exception("test log location is not a directory")

    val workDir = new File(testDir)

    if (!workDir.exists()) workDir.mkdir()
    else throw new Exception("Test dir not empty" + testDir)

    testLogLocation.listFiles().filter(f).foreach {
      case file: File => cp(file, workDir.getAbsolutePath, file.getName)
    }
  }
}
