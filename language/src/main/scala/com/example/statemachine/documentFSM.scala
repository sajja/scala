package com.example.statemachine

import scala.util.Random

class AbstactState()

case class NewState() extends AbstactState

class Operation()

case class ReceveDocument() extends Operation

case class BatchDocument() extends Operation

case class SignDocument() extends Operation

case class Document(id: Int, state: String) {}

object TestDocumentFSM {
  def main(args: Array[String]): Unit = {
    val storage = Storage()
    val docFsm = new MyFSM[Operation, Document](Document(0, "R"),
      (document: Document, operation: Operation) => (operation, document) match {
        case (ReceveDocument(), _) => new DocumentDao(storage).newDocument(document)
        case (BatchDocument(), _) => new DocumentBatcherService(storage).batchDocument(document)
        case (SignDocument(), _) => new SignService(storage).sign(document)
      }
    )
    val st = docFsm.runState(ReceveDocument()).runState(SignDocument()).runState(BatchDocument())
    println(st.state)
  }
}

class Storage {
  val storage: scala.collection.mutable.Map[Int, Document] = scala.collection.mutable.Map()
}

object Storage {
  def apply(): Storage = new Storage()
}

class DocumentDao(storage: Storage) {
  def newDocument(document: Document): Document = {
    val doc = Document(Random.nextInt(), "N")
    storage.storage.put(doc.id, doc)
    doc
  }

  def loadDocument(id: Int): Option[Document] = storage.storage.get(id)
}

class DocumentBatcherService(storage: Storage) {
  def batchDocument(document: Document): Document = {
    val doc = Document(document.id, "B")
    storage.storage.put(document.id, doc)
    doc
  }
}

class SignService(storage: Storage) {
  def sign(document: Document): Document = {
    val doc = Document(document.id, "S")
    storage.storage.put(document.id, doc)
    doc
  }
}