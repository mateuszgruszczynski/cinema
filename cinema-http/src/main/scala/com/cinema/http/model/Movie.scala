package com.cinema.http.model

import com.cinema.http.DBHandler
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Movie(
  id: Option[Int],
  title: String,
  director: String,
  genre: String,
  country: String,
  year: Int,
  tags: String,
  agelimit: Int
)

object MoviesDB extends DBHandler {
  
  import scala.concurrent.ExecutionContext.Implicits.global

  class Movies(tag: Tag) extends Table[Movie](tag, "movies"){

    def id = column[Int]("id",O.AutoInc, O.PrimaryKey)
    def title = column[String]("title")
    def director = column[String]("director")
    def genre = column[String]("genre")
    def country = column[String]("country")
    def year = column[Int]("year")
    def tags = column[String]("tags")
    def ageLimit = column[Int]("agelimit")

    def * = (id.?, title, director, genre, country, year, tags, ageLimit) <> (Movie.tupled, Movie.unapply)
  }

  val movies = TableQuery[Movies]

  def addMovie(movieEntity: Movie) = awaitQuery[Movie](
    db.run(movies returning movies.map(_.id) += movieEntity)
      .map(id => movieEntity.copy(id = Some(id)))
  )

  def getMovie(movieId: Int) = {
    awaitQuery[Option[Movie]](
      db.run(movies.filter(_.id === movieId).result.headOption)
    )
  }

  def updateMovie(walletEntity: Movie) = awaitQuery[Int](
    db.run(movies.insertOrUpdate(walletEntity))
  )

  def findMovieByTitle(title: String) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.title like s"%${title}%").result)
  )

  def findMovieByTitlePage(title: String, page: Int) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.title like s"%${title}%").drop((page-1)*10).take(10).result)
  )

  def findMovieByGenre(genre: String) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.genre === genre).result)
  )

  def findMovieByGenrePage(genre: String, page: Int) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.genre === genre).drop((page-1)*10).take(10).result)
  )

  def findMovieByTitleAndGenre(title: String, genre: String) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.title like s"%${title}%").filter(_.genre === genre).result)
  )

  def findMovieByTitleAndGenrePage(title: String, genre: String, page: Int) = awaitQuery[Seq[Movie]](
    db.run(movies.filter(_.title like s"%${title}%").filter(_.genre === genre).drop((page-1)*10).take(10).result)
  )

  def findAllMovies = awaitQuery[Seq[Movie]](
    db.run(movies.result)
  )

  def findMoviesPage(page: Int = 1) = awaitQuery[Seq[Movie]](
    db.run(movies.drop((page-1)*10).take(10).result)
  )
}