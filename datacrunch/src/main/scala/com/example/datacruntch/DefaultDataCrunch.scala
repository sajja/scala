package com.example.datacruntch

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

/**
 * Created by sajith on 7/25/15.
 */
object DefaultDataCrunch {

  def crunch(file:File) =  {}
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
    cal.add(Calendar.MONTH,2)
    val to = cal.getTime

    DefaultDataCrunch.crunch("/home/sajith/scratch/scala/datacrunch/logs",date,to)
  }


}
