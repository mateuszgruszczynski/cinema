package com.cinema.http.model

import slick.jdbc.PostgresProfile.api._
import com.cinema.http.DBHandler
import slick.lifted.Tag

case class User(
  id: Option[Int] = None,
  firstname: String,
  lastname: String,
  email: String,
  username: String,
  city: String,
  address: String,
  birthdate: String,
  password: Option[String] = None,
  token: Option[String] = None,
){
  def toSecret = this.copy(password = None, token = None)
}

object UsersDB extends DBHandler {

  import scala.concurrent.ExecutionContext.Implicits.global

  class Users(tag: Tag) extends Table[User](tag, "users"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("firstname")
    def lastName = column[String]("lastname")
    def email = column[String]("email")
    def userName = column[String]("username")
    def city = column[String]("city")
    def address = column[String]("address")
    def birthDate = column[String]("birthdate")
    def passHash = column[String]("passhash")
    def token = column[String]("token")

    def * = (id.?, firstName, lastName, email, userName, city, address, birthDate, passHash.?, token.?) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def findUsersPage(page: Int = 1) = awaitQuery[Seq[User]](
    db.run(users.drop((page - 1) * 10).take(10).result)
  )

  def findUsers: Seq[User] = awaitQuery[Seq[User]](
    db.run(users.result)
  )

  def getUserById(id: Int) : Option[User] = awaitQuery[Option[User]](
    db.run(users.filter(_.id === id).result.headOption)
  )

  def getUserByUserName(username: String) : Option[User] = awaitQuery[Option[User]](
    db.run(users.filter(_.userName === username).result.headOption)
  )

  def getUserByEmail(email: String) : Option[User] = awaitQuery[Option[User]](
    db.run(users.filter(_.email === email).result.headOption)
  )

  def addUser(u: User) = awaitQuery[User](
    db.run(users returning users.map(_.id) += u)
      .map(id => u.copy(id = Some(id)))
  )

  def updateUser(uid: Int, u: User) = awaitQuery[Int](
    db.run(users.insertOrUpdate(u))
  )
}