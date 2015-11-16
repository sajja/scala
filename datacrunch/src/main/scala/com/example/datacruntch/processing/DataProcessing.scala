package com.example.datacruntch.processing

import java.io.{File, FileInputStream, InputStream}
import java.text.SimpleDateFormat
import java.util.Calendar

import com.example.datacruntch.storage.{EventStorageModule, FileSystemModule}

import scala.collection.immutable.Iterable
import scala.io.Source
import scala.util.Try

trait DataProcessingModule extends FileSystemModule with DomainModelModule {
  this: EventStorageModule with Algorithms =>

  object LogProcessingService {
    def processLogs[A <: KeyLike](logDir: String, dateFormat: String)(mapFn: Mapped[A])(reduceFn: Reduced[A])(transformFn: (A, Int) => DomainObject)(implicit source: String) = {
      val df = new SimpleDateFormat(dateFormat)

      listFiles(logDir)(_.isFile) foreach {
        file => {
          for {
            fis <- loadFile(file)
            processedData <- extract(fis, df)(mapFn)(reduceFn)(source + "_" + file.getName)
            transformedData <- transform(processedData)(transformFn)
            count <- load(transformedData)(file.getName)
            _ <- postProcess(file)
          } yield {}
        }
      }
    }

    def loadFile(file: File) = Try(new FileInputStream(file))

    def extract[A <: KeyLike](fis: InputStream, df: SimpleDateFormat)(mapFn: Mapped[A])(reduceFn: Reduced[A])(source: String): Try[Map[A, Int]] = {
      Try {
        val lines = Source.fromInputStream(fis).getLines()
        reduceFn(lines.map(l => mapFn(l, source)))
      }
    }

    def transform[A <: KeyLike](processedRows: Map[A, Int])(transformerFn: (A, Int) => DomainObject) =
      Try(processedRows.map(data => transformerFn(data._1, data._2)))

    def load(transformedRows: Iterable[DomainObject])(fileName:String) = {
      val triedLoad = store(transformedRows)
      if (triedLoad.isSuccess) println(s"${transformedRows.size} rows sent for storing $fileName" )
      triedLoad
    }

    def postProcess(file: File) = {
      rm(file)
    }
  }

}

trait Algorithms

trait EventProcessingAlgorithms extends Algorithms with DomainModelModule {

  object algo {
    def reduce(mappedVals: Iterator[Try[(Key, Int)]]) = {
      mappedVals.foldLeft(Map[Key, Int]()) { (agg: Map[Key, Int], tuple: Try[(Key, Int)]) =>
        if (tuple.isSuccess) {
          val row = tuple.get
          val item = agg.get(row._1)

          item match {
            case Some(a) => agg ++ Map(row._1 -> (1 + a))
            case None => agg ++ Map(row._1 -> 1)
          }
        } else agg
      }
    }

    def mapper(line: String, source: String) = {
      Try {
        val tokens = line.split(" ")
        val calendar = Calendar.getInstance()
        val df = new SimpleDateFormat("MM-dd-yyyy-HH:mm")
        val date = df.parse(tokens(0))
        calendar.setTime(date)
        calendar.set(Calendar.MINUTE, 0)
        (new Key(calendar.getTime, tokens(1).toInt, source, tokens(2)), 1)
      }
    }

    def convert(key: Key, count: Int) = new Event(key.date, key.customerId, key.inputSource, key.event, count)
  }

}
