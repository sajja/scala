package com.example.datacruntch.storage

import java.io.File
import java.util.Date

import com.example.datacruntch.processing.DomainModelModule

import scala.util.Try


trait EventStorageModule extends DomainModelModule {
  def store(e: DomainObject): Unit

  def store(listOf: Iterable[DomainObject]): Try[Unit]

  def loadByDate(date: Date): List[DomainObject]

  def bootstrap(events: List[DomainObject]): Try[Unit]
}

trait FileSystemModule {

  import java.nio.file.Files.{copy, delete}
  import java.nio.file.Paths.get

  def listFiles(uri: String)(filter: File => Boolean): Array[File] = {
    val dir = new File(uri)
    dir.listFiles().filter(filter)
  }

  def rm(file: File): Try[Unit] = {
    Try {
      if (file.isDirectory) new Exception("I cannot delete folders")
      else delete(get(file.getAbsolutePath))
    }
  }

  def cp(source: File, dest: String, destName: String) = {
    val destination = new File(dest)

    if (destination.exists() && destination.isDirectory && destName != null) copy(get(source.getAbsolutePath), get(destination.getAbsolutePath + "/" + destName))
    else throw new Exception("Unable to copy")
  }
}
