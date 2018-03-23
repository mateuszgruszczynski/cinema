package com.cinema.http.model

import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag
import com.cinema.http.DBHandler
import com.cinema.http.model.WalletsDB.Wallets

case class Screening(
  id: Option[Int] = None,
  movieid: Int,
  city: String,
  roomnumber: Int,
  seatslimit: Int,
  seatstaken: Int,
  time: String,
  price: BigDecimal
)

object ScreeningsDB extends DBHandler {

  import scala.concurrent.ExecutionContext.Implicits.global

  class Screenings(tag: Tag) extends Table[Screening](tag, "screenings"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def movieId = column[Int]("movieId")
    def city = column[String]("city")
    def roomNumber = column[Int]("roomNumber")
    def seatsLimit = column[Int]("seatsLimit")
    def seatsTaken = column[Int]("seatsTaken")
    def time = column[String]("time")
    def price = column[BigDecimal]("price")

    def * = (id.?, movieId, city, roomNumber, seatsLimit, seatsTaken, time, price) <> (Screening.tupled, Screening.unapply)
  }

  val screenings = TableQuery[Screenings]

  def addScreening(screeningEntity: Screening) = awaitQuery[Screening](
    db.run(screenings returning screenings.map(_.id) += screeningEntity)
      .map(id => screeningEntity.copy(id = Some(id)))
  )

  def getAllScreenings = awaitQuery[Seq[Screening]](
    db.run(screenings.result)
  )

  def getAllScreeningsPage(page: Int) = awaitQuery[Seq[Screening]](
    db.run(screenings.drop((page -1) *10).take(10).result)
  )

  def getScreeningById(screeningId: Int) = awaitQuery[Option[Screening]](
    db.run(screenings.filter(_.id === screeningId).result.headOption)
  )

  def getScreeningsByMovieId(movieId: Int) = awaitQuery[Seq[Screening]](
    db.run(screenings.filter(_.movieId === movieId).result)
  )

  def getScreeningsByMovieIdPage(movieId: Int, page: Int) = awaitQuery[Seq[Screening]](
    db.run(screenings.filter(_.movieId === movieId).drop((page -1) *10).take(10).result)
  )
}