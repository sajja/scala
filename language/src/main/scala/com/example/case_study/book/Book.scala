package com.example.case_study.book

case class Item()

case class Book(isbn: String, name: String, author: String) extends Item

case class CD(titel: String) extends Item

case class Catalog(name: String, books: Array[Book])

case class Reservation(item: Item, customer: Customer)

case class Customer(id: String, name: String)


class BookRepo {
  var books:Seq[Book] = Seq()
}

trait BookService {
  def addBook(isbn: String, name: String, author: String): BookRepo = {
  }

  def getBookByIsbn(isbn: String): Book = _

  def getBookByAuthor(name: String): Array[Book] = _
}



trait ItemRepo {
  var books: Seq[Book] = Seq()
  var cds: Seq[CD] = Seq()
  val reservations: Seq[]
}

object BookingSystem {
  def main(args: Array[String]): Unit = {

  }
}
