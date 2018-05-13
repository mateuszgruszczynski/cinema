package com.cinema.http

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Awaitable}

class DBHandler {
  import scala.concurrent.duration._
  val db = Database.forConfig("psql")
  val dbTimeout = 30 seconds
  def awaitQuery[T](a: Awaitable[Any]) : T = Await.result(a, dbTimeout).asInstanceOf[T]
}