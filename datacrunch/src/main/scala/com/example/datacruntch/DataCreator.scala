package com.example.datacruntch

import java.io.{File, FileOutputStream}
import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date}


import scala.util.Random

object DataCreator {
  val eventTypes = Seq("CREATE", "UPDATE", "ARCHIVED")
  val MAX_DOC = 100
  val MAX_EVENTS_PER_DOC = 1000

  def generateEvents(docId: Int, amount: Int, date: Date) = {
    val rand = new Random
    val current = date
    val df = new SimpleDateFormat("dd-MM-yyyy-HH:mm")
    var str = ""
    val cal = Calendar.getInstance()
    for (i <- 0 to amount) {
      cal.setTime(date)
      cal.set(Calendar.HOUR_OF_DAY, getPositiveRandom(24))
      cal.set(Calendar.MINUTE, getPositiveRandom(60))
      str += df.format(cal.getTime) + " " + docId + " " + eventTypes(getPositiveRandom(3))
      str += "\n"
    }
    str
  }

  def generateRandomData(date: Date) = {
    val maxDocId = getPositiveRandom(MAX_DOC)
    val numIter = getPositiveRandom(MAX_EVENTS_PER_DOC)
    var str = ""
    for (i <- 1 to maxDocId) {
      str += generateEvents(i, numIter, date)
    }
    str
  }

  def getPositiveRandom(max: Int): Int = {
    val rand = new Random
    val num = rand.nextInt() % max
    if (num == 0) getPositiveRandom(max)
    else if (num < 0)
      num * -1
    else
      num
  }

  def createDataSet(from: Date, to: Date, dir: String) = {
    var current = from
    val cal = Calendar.getInstance()
    cal.setTime(from)
    val df = new SimpleDateFormat("dd-MM-yyyy")
    while (current.before(to)) {
      val fileName = df.format(current)
      val data = generateRandomData(current)
      val fos = new FileOutputStream(new File(dir + "/" + fileName))

      println("writing to file....")
      fos.write(data.getBytes())
      fos.close()

      cal.add(Calendar.DATE, 1)
      current = cal.getTime()

    }
  }

  def main(args: Array[String]) {
    val cal = Calendar.getInstance()
    val start = cal.getTime
    cal.add(Calendar.DATE, 100)
    val dirStr = "/home/sajith/scratch/scala/datacrunch/logs"
    val dir = new File(dirStr)

    println("cleaning up log dir...")
    for (x <- dir.listFiles()) {
      x.delete()
    }

    createDataSet(start, cal.getTime, "/home/sajith/scratch/scala/datacrunch/logs")
  }
}
