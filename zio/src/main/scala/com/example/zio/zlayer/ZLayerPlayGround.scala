package com.example.zio.zlayer

import zio.{Console, Scope, Task, UIO, ZIO, ZIOAppArgs, ZLayer}
import zio.ZIO.console
import zio.metrics.jvm.DefaultJvmMetrics.app

import java.io.IOException
import scala.util.Random

case class Doc(title: String, description: String, language: String, format: String, content: Array[Byte])

case class Metadata(title: String, description: String, language: String, format: String)

trait DocRepo {
  def get(id: String): ZIO[Any, Throwable, Doc]

  def save(document: Doc): ZIO[Any, Throwable, String]

  def delete(id: String): ZIO[Any, Throwable, Unit]

  def findByTitle(title: String): ZIO[Any, Throwable, List[Doc]]
}

trait MetadataRepo {
  def get(id: String): ZIO[Any, Throwable, Metadata]

  def put(id: String, metadata: Metadata): ZIO[Any, Throwable, Unit]

  def delete(id: String): ZIO[Any, Throwable, Unit]

  def findByTitle(title: String): ZIO[Any, Throwable, Map[String, Metadata]]
}

trait BlobStorage {
  def get(id: String): ZIO[Any, Throwable, Array[Byte]]

  def put(content: Array[Byte]): ZIO[Any, Throwable, String]

  def delete(id: String): ZIO[Any, Throwable, Unit]
}

case class BlobStorageImpl() extends BlobStorage {
  override def get(id: String): ZIO[Any, Throwable, Array[Byte]] = {
    ZIO.from {
      Random.nextString(10).getBytes()
    }
  }

  override def put(content: Array[Byte]): ZIO[Any, Throwable, String] = ZIO.from {
    new String(content)
  }

  override def delete(id: String): ZIO[Any, Throwable, Unit] = ZIO.from {}
}

case class MetadataRepoImpl() extends MetadataRepo {
  override def get(id: String): ZIO[Any, Throwable, Metadata] = ZIO.from {
    Metadata(s"$id:Title", s"$id:Descriptoin", "EN", "PDF")
  }

  override def put(id: String, metadata: Metadata): ZIO[Any, Throwable, Unit] = ZIO.from {
  }

  override def delete(id: String): ZIO[Any, Throwable, Unit] = ZIO.from {}

  override def findByTitle(title: String): ZIO[Any, Throwable, Map[String, Metadata]] = ZIO.from {
    Map(title -> Metadata(title, s"$title:Description", "EN", "PDF"))
  }
}

case class DocRepoImpl(metadataRepo: MetadataRepo, blobStorage: BlobStorage) extends DocRepo {
  override def get(id: String): ZIO[Any, Throwable, Doc] =
    for {
      metadata <- metadataRepo.get(id)
      content <- blobStorage.get(id)
    } yield Doc(metadata.title, metadata.description, metadata.language, metadata.format, content)

  override def save(document: Doc): ZIO[Any, Throwable, String] =
    for {
      id <- blobStorage.put(document.content)
      _ <- metadataRepo.put(id, Metadata(document.title, document.description, document.language, document.format))
    } yield id

  override def delete(id: String): ZIO[Any, Throwable, Unit] =
    for {
      _ <- blobStorage.delete(id)
      _ <- metadataRepo.delete(id)
    } yield ()

  override def findByTitle(title: String): ZIO[Any, Throwable, List[Doc]] =
    for {
      map <- metadataRepo.findByTitle(title)
      content <- ZIO.foreach(map)((id, metadata) =>
        for {
          content <- blobStorage.get(id)
        } yield id -> Doc(metadata.title, metadata.description, metadata.language, metadata.format, content)
      )
    } yield content.values.toList
}

object DocRepoImpl {
  val layer: ZLayer[BlobStorage with MetadataRepo, Nothing, DocRepo] = {
    ZLayer {
      for {
        metadataRepo <- ZIO.service[MetadataRepo]
        blobStorage <- ZIO.service[BlobStorage]
      } yield DocRepoImpl(metadataRepo, blobStorage)
    }
  }
}

object MetadataRepoImpl {
  val layer: ZLayer[Any, Nothing, MetadataRepo] = ZLayer {
    ZIO.succeed(MetadataRepoImpl())
  }
}

object BlobStorageImpl {
  val layer: ZLayer[Any, Nothing, BlobStorage] = ZLayer {
    ZIO.succeed(BlobStorageImpl())
  }
}

object DocRepo {
  def get(id: String): ZIO[DocRepo, Throwable, Doc] =
    ZIO.serviceWithZIO[DocRepo](_.get(id))

  def save(document: Doc): ZIO[DocRepo, Throwable, String] =
    ZIO.serviceWithZIO[DocRepo](_.save(document))

  def delete(id: String): ZIO[DocRepo, Throwable, Unit] =
    ZIO.serviceWithZIO[DocRepo](_.delete(id))

  def findByTitle(title: String): ZIO[DocRepo, Throwable, List[Doc]] =
    ZIO.serviceWithZIO[DocRepo](_.findByTitle(title))
}

object ZLayerPlayGround extends zio.ZIOAppDefault {

  def call1() = {
    val v = for {
      v <- DocRepo.get("12").provide(DocRepoImpl.layer, BlobStorageImpl.layer, MetadataRepoImpl.layer)
    } yield v
    for {
      d <- v
      _ <- Console.printLine(d.title)
    } yield ()
  }

  def call2() = {
    for {
      doc <- ZIO.serviceWithZIO[DocRepo](_.get("1111")).provide(DocRepoImpl.layer, BlobStorageImpl.layer, MetadataRepoImpl.layer)
      a <- Console.printLine(doc.title)
    } yield ()
  }

  override def run = {
    val i = call2()
    val j = call1()
    i
  }
}
