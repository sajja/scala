package com.example.datacruntch

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import scala.util.Random

/**
  * Created by sajith on 7/25/15.
  */
object DefaultDataCrunch {
  val maxEventsPerDoc = 5
  val maxEventsPerCust = 1000

  def crunch(file: File) = {}

  def crunch(dir: String, from: Date, to: Date) = {
    val df = new SimpleDateFormat("dd-MM-yyyy")
    val cal = Calendar.getInstance()
    var curr = from

    while (curr.before(to)) {
      cal.setTime(curr)
      val fileName = df.format(curr)
      cal.add(Calendar.DATE, 1)
      curr = cal.getTime
      val file = new File(dir + "/" + fileName)
      //      if (file.exists()) crunch(file)
    }
  }

  def main(args: Array[String]) {
    val cal = Calendar.getInstance()
    val date = cal.getTime
    cal.add(Calendar.MONTH, 2)
    val to = cal.getTime

    DefaultDataCrunch.crunch("/home/sajith/scratch/scala/datacrunch/logs", date, to)
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
}
