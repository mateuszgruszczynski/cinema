package com.cinema.http

import com.cinema.http.model.UsersDB
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, Awaitable}

class DBHandler {
  import scala.concurrent.duration._
  val db = Database.forConfig("mysql")
  val dbTimeout = 30 seconds
  def awaitQuery[T](a: Awaitable[Any]) : T = Await.result(a, dbTimeout).asInstanceOf[T]
}


object DBTest extends App {

  System.out.println("Login:")
  val email = readLine()

  System.out.println("Pass")
  val pass = readLine()

  UsersDB.getUserByEmail(email) match {
    case Some(u) => if(u.password == Some(pass)) println(s"Welcome ${u.firstname}!") else println("Invalid credentials")
    case None => println("Invalid credentials")
  }
}