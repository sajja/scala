
import com.example.slick.util.DatabaseWrapper
import slick.dbio.DBIOAction
import slick.dbio.Effect.Write
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random


class T1s(tag: Tag) extends Table[T1](tag, "T1") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[String]("NAME")

  def * = (id, name) <> (T1.tupled, T1.unapply _)
}

class T2s(tag: Tag) extends Table[T2](tag, "T2") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[Int]("T1Name")

  def * = (id, name) <> (T2.tupled, T2.unapply _)
}

class T3s(tag: Tag) extends Table[T3](tag, "T3") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[Int]("T2Name")

  def * = (id, name) <> (T3.tupled, T3.unapply _)
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[String]("NAME")

  def * = (id, name) <> (User.tupled, User.unapply _)
}

case class T1(id: Int, name: String)

case class T2(id: Int, name: Int)

case class T3(id: Int, name: Int)

class Addresses(tag: Tag) extends Table[Address](tag, "ADDRESSES") {
  def id = column[Int]("ID", O.PrimaryKey)

  def street = column[String]("STREET")

  def user_id = column[Int]("USER_ID")

  def * = (id, street, user_id) <> (Address.tupled, Address.unapply _)
}

case class User(id: Int, name: String)

case class Address(id: Int, street: String, user_id: Int)

class UserDao extends Db {
  def findUser(name: String): DBIOAction[Seq[User], NoStream, Effect.Read] = {
    users.filter(_.name === name).result
  }

  def updateUser(user: User): DBIOAction[Int, NoStream, Effect.Write] = {
    users.filter(_.id === user.id).map(_.name).update(user.name)
  }
}

class AddressDao extends Db {
  def findAddress(id: Int): DBIOAction[Seq[Address], NoStream, Effect.Read] = {
    addresses.filter(_.user_id === id).result
  }

  def findAddress(user: User): DBIOAction[Seq[Address], NoStream, Effect.Read] = {
    addresses.filter(_.user_id === user.id).result
  }

  def updateAddress(address: Address): DBIOAction[Int, NoStream, Effect.Write] = {
    addresses.filter(_.id === address.id).map(_.street).update(address.street + "1")
  }

  def createAddress(id: Int, user: User): DBIOAction[Option[Address], NoStream, Effect.Write] = {
    println(s"Creating address $id for user id ${user.id}")
    (addresses returning addresses).insertOrUpdate(Address(Random.nextInt(100), user.name + id, user.id))
  }
}


class UserService {
  //  def findUser(id: Int): Future[Option[User]] = _

  //  def findAddress(user: User): Future[Option[Address]] = _
}

class Db {
  val db = DatabaseWrapper.db
  val users = TableQuery[Users]
  val t1s = TableQuery[T1s]
  val t2s = TableQuery[T2s]
  val t3s = TableQuery[T3s]
  val addresses = TableQuery[Addresses]
}

object TestDB extends Db {
  def bootstrap() = {
    val db = DatabaseWrapper.db
    val setup = DBIO.seq(
      users.schema.drop,
      addresses.schema.drop,
      users.schema.create,
      addresses.schema.create,
      t1s.schema.drop,
      t2s.schema.drop,
      t3s.schema.drop,
      t1s.schema.create,
      t2s.schema.create,
      t3s.schema.create,
      users += User(1, "sajith"),
      users += User(2, "sajith"),
      users += User(3, "Silva")
      //      addresses += Address(2, "Katubadda", 1),
      //      addresses += Address(3, "Rathmalana", 1),
      //      addresses += Address(4, "Sweden", 2),
      //      addresses += Address(5, "Melbourne", 2)
    )

    Await.result(db.run(setup), 10 seconds)
  }

  def main(args: Array[String]) {

    def createT2(t2: T2): DBIOAction[Option[T2], NoStream, Write] = {
      val xt21: DBIOAction[T2, NoStream, Write] = (t2s returning t2s) += t2
      val xt22: DBIOAction[Option[T2], NoStream, Write] = (t2s returning t2s).insertOrUpdate(t2)
      val xt23: DBIOAction[Seq[T2], NoStream, Write] = (t2s returning t2s) ++= List(t2)
      xt22
    }

    def createT2s(t2s: List[T2]): DBIOAction[List[Option[T2]], NoStream, Write] = {
      val t21: List[DBIOAction[Option[T2], NoStream, Write]] = t2s.map(t2 => createT2(t2))
      val t23: DBIOAction[List[Option[T2]], NoStream, Write] = DBIO.sequence(t21)
      t23
    }

    import scala.concurrent.ExecutionContext.Implicits.global
    bootstrap()
    val l1: List[Int] = List(1, 2)
    val l2: List[Int] = List(4, 5, 6)


    //basic working
    var y = for {
      t1o <- (t1s returning t1s).insertOrUpdate(T1(111, "1000"))
      x <- t1o match {
        case Some(t1) => (t2s returning t2s) ++= l1.map(i => T2(i, t1.id))
      }
      y <- (t3s returning t3s) ++= x.flatMap(j => l2.map(k => T3(Random.nextInt(), j.id)))
    } yield ()


    y = for {
      t1o <- (t1s returning t1s).insertOrUpdate(T1(111, "1000"))
      x <- t1o match {
        case Some(t1) => createT2s(l1.map(i=>T2(i,t1.id)))
      }
      y <- (t3s returning t3s) ++= x.flatMap(j => l2.map(k => T3(Random.nextInt(), j.get.id)))
    } yield ()

    /*
        val g: DBIOAction[Seq[T2], NoStream, Write] = (t2s returning t2s) ++= l1.map(i => T2(i, 10))
        y = (t1s returning t1s).insertOrUpdate(T1(111, "1000")).
          flatMap((tio: Option[T1]) => g.map((ts: Seq[T2]) => ts)).
          flatMap((x: Seq[T2]) => {
            val v: Seq[T3] = x.map(l => T3(Random.nextInt(), l.id))
            val b = v.map(t3 => l2.map(i => T3(Random.nextInt(), 999))).flatten
            println("-------------------")
            println(s"v = $v")
            println(s"b = $b")
            println("-------------------")
            (t3s returning t3s) ++= b
          }.map((result: Seq[T3]) => result))
    */

    val r0 = Await.result(db.run(y), 10 seconds)

    println("T1---------------------")
    val r1 = Await.result(db.run(t1s.result), 10 seconds)
    println(r1)
    println("T2---------------------")
    val r2 = Await.result(db.run(t2s.result), 10 seconds)
    println(r2)
    println("T3---------------------")
    val r3 = Await.result(db.run(t3s.result), 10 seconds)
    println(r3)
    val r4 = Await.result(db.run(users.filter(_.id === -21).result.headOption),4 second)
    println(r4)


  }
}

