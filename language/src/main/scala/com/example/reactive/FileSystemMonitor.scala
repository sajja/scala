import java.io.File

import rx.lang.scala.Observable

trait FileSystemMonitor {
  type FS = Map[String, Long]

  case class State[S, A](run: (S) => (S, A))

  def monitor(path: String): State[FS, Set[(String, String)]] = {
    State[FS, Set[(String, String)]]((fs: FS) => {
      val newMap: FS = Map(new File(path).listFiles() map { s => (s.getName, s.lastModified()) }: _*) //_* will convert the result into a variable argument.
      val added = newMap.keySet.diff(fs.keySet).map((_, "ADDED"))
      val deleted = fs.keySet.diff(newMap.keySet).map((_, "DELETED"))
      val modified = newMap.keySet.intersect(fs.keySet).map(name => {
        val m = for {
          nT <- newMap.get(name)
          oT <- fs.get(name)
        } yield nT != oT

        (name, m.get)
      }).filter((tuple: (String, Boolean)) => tuple._2).map((tuple: (String, Boolean)) => (tuple._1, "MODIFIED"))
      (newMap, added ++ deleted ++ modified)
    })
  }

  def monitorFolder(path: String, delay: Int): Stream[State[FS, Set[(String, String)]]] = {
    Stream.cons(monitor(path), {
      Thread.sleep(delay)
      monitorFolder(path, delay)
    })

  }
}


object TestStaetMachine {

  def main(args: Array[String]): Unit = {
    val fs = new FileSystemMonitor {}
    val fsm = fs.monitorFolder("/home/sajith/tmp",3000)
    val fsmObs = Observable.from(fsm)
    var oldState = Map[String, Long]()
    fsmObs.foreach((state: fs.State[fs.FS, Set[(String, String)]]) => {
      val st = state.run(oldState)
      println(st._2.mkString("\n"))
      oldState = st._1
    })

  }
}
