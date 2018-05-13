package com.cinema.http

import java.sql.SQLIntegrityConstraintViolationException
import java.util.UUID

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{RequestContext, Route}
import com.cinema.http.Routes.complete
import com.cinema.http.model._
import org.json4s._
import org.json4s.jackson.Serialization.write

import scala.util.Random

object Security {

  implicit val formats = DefaultFormats

  def makeHash(s: String) = {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = s.getBytes("UTF-8")
    m.update(b, 0, b.length)
    new java.math.BigInteger(1, m.digest()).toString(16)
  }

  def checkSession(userId: Int, context: RequestContext, onSuccess: Route) : Route = {
    UsersDB.getUserById(userId) match {
      case Some(u) => {
        context.request.headers.find(h => h.name.toLowerCase == "http-auth-token") match {
          case Some(h) => if(u.token == Some(h.value())) onSuccess else complete(StatusCodes.Unauthorized -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid or missing session token"))))
          case None => complete(StatusCodes.Unauthorized -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid or missing session token"))))
        }
      }
      case None => {
        complete(StatusCodes.Unauthorized -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid or missing session token"))))
      }
    }
  }
}

case class Error(
  message: String
)

case class Server(id: Int)

object ServerStatus{
  val serverId = Random.nextInt(999)
  val server = Server(serverId)
}

object RoutesHandler {

  implicit val formats = DefaultFormats

  def getAllUsers(page: Option[Int] = None): Route = {
    val users : Seq[User] = page match {
      case Some(p) => for(u <- UsersDB.findUsersPage(p)) yield u.toSecret
      case _ => for(u <- UsersDB.findUsers) yield u.toSecret
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(users)))
  }

  def getUserById(userId: Int): Route = {
    UsersDB.getUserById(userId) match {
      case Some(u) => complete(HttpEntity(ContentTypes.`application/json`, write(u.toSecret)))
      case _ => complete(StatusCodes.NotFound)
    }
  }

  def addUser(userEntity: User): Route = {
    val user = userEntity.copy(password = Some(Security.makeHash(userEntity.password.get)))
    try {
      val addedUser = UsersDB.addUser(user)
      val wallet = WalletsDB.addWallet(Wallet(None, addedUser.id.get, 0.0))
      complete(StatusCodes.Created -> HttpEntity(ContentTypes.`application/json`, write(addedUser.toSecret)))
    } catch {
      case e: Exception if e.getMessage.contains("duplicate key")=> {
        complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Username or email already registered"))))
      }
    }
  }

  def updateUser(userId: Int, userEntity: User): Route = {
    UsersDB.getUserById(userId) match {
      case Some(u) => {
        UsersDB.updateUser(userId, userEntity.copy(id = u.id, email = u.email, username = u.username, password = Some(Security.makeHash(userEntity.password.getOrElse(u.password.get))), token = u.token))
        val updatedUser = UsersDB.getUserById(userId).get
        complete(StatusCodes.OK -> HttpEntity(ContentTypes.`application/json`, write(updatedUser.toSecret)))
      }
      case None => complete(StatusCodes.NotFound)
    }
  }

  def authenticate(authEntity: Authenticate): Route = {
    UsersDB.getUserByUserName(authEntity.username) match {
      case Some(u) => {
        (u.password.get == Security.makeHash(authEntity.password)) match {
          case true => {
            val sessionToken = UUID.randomUUID().toString
            UsersDB.updateUser(u.id.get, u.copy(token = Some(sessionToken)))
            complete(HttpEntity(ContentTypes.`application/json`, write(AuthenticateResponse(u.toSecret, sessionToken))))
          }
          case _ => complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid credentials"))))
        }
      }
      case None => complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid credentials"))))
    }
  }

  def getUserWallet(userId: Int): Route = {
    WalletsDB.getWallet(userId) match {
      case Some(w) => complete(HttpEntity(ContentTypes.`application/json`, write(w)))
      case None => complete(StatusCodes.NotFound)
    }
  }

  def increaseUserWallet(userId: Int, value: BigDecimal): Route = {
    WalletsDB.getWallet(userId) match {
      case Some(w) => {
        WalletsDB.updateWallet(w.copy(value = w.value + value))
        complete(StatusCodes.OK)
      }
      case None => complete(StatusCodes.NotFound)
    }
  }

  def placeOrder(orderEntity: Order): Route = {
    ScreeningsDB.getScreeningById(orderEntity.screeningid) match {
      case Some(s) => {
        val order = OrdersDB.addOrder(orderEntity.copy(status = Some("BOOKED"), totalprice = Some(s.price * orderEntity.ticketscount)))
        complete(HttpEntity(ContentTypes.`application/json`, write(order)))
      }
      case None => complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid screening"))))
    }
  }

  def payOrder(orderId: Int) : Route = {
    OrdersDB.getOrder(orderId) match {
      case Some(o) => {
        if(o.status == Some("PAID")) return complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Order already paid"))))
        WalletsDB.getWallet(o.userid) match {
          case Some(w) => w.value >= o.totalprice.getOrElse(0) match {
            case true => {
              WalletsDB.updateWallet(w.copy(value = w.value - o.totalprice.getOrElse(0)))
              OrdersDB.updateOrder(o.copy(status = Some("PAID")))
              complete(HttpEntity(ContentTypes.`application/json`, write(o.copy(status = Some("PAID")))))
            }
            case false => {
              complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Insufficient funds in wallet"))))
            }
          }
          case None => complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid user wallet"))))
        }
      }
      case None => complete(StatusCodes.NotFound)
    }
  }

  def getAllOrders(page: Option[Int] = None): Route = {
    val orders = page match {
      case Some(p) => OrdersDB.getAllOrdersPage(p)
      case None => OrdersDB.getAllOrders
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(orders)))
  }

  def getOrder(orderId: Int): Route = {
    OrdersDB.getOrder(orderId) match {
      case Some(o) => complete(HttpEntity(ContentTypes.`application/json`, write(o)))
      case None => complete(StatusCodes.NotFound)
    }
  }

  def getUserOrder(userId: Int, orderId: Int): Route = {
    OrdersDB.getOrder(orderId) match {
      case Some(o) => (o.userid == userId) match {
        case true => complete(HttpEntity (ContentTypes.`application/json`, write (o) ) )
        case false => complete(StatusCodes.NotFound)
      }
      case None => complete(StatusCodes.NotFound)
    }
  }

  def getScreeningsOrders(screeningId: Int, page: Option[Int] = None) = {
    val orders = page match {
      case Some(p) => OrdersDB.getScreeningOrdersPage(screeningId, p)
      case None => OrdersDB.getScreeningOrders(screeningId)
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(orders)))
  }

  def getUserOrders(userId: Int, page: Option[Int] = None): Route = {
    val orders = page match {
      case Some(p) => OrdersDB.getUserOrdersPage(userId, p)
      case None => OrdersDB.getUserOrders(userId)
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(orders)))
  }

  def addMovie(movieEntity: Movie): Route = {
    val movie = MoviesDB.addMovie(movieEntity)
    complete(HttpEntity(ContentTypes.`application/json`, write(movie)))
  }

  def getMovie(movieId: Int): Route = {
    MoviesDB.getMovie(movieId) match {
      case Some(m) => complete(HttpEntity(ContentTypes.`application/json`, write(m)))
      case _ => complete(StatusCodes.NotFound)
    }
  }

  def getMovies(page: Option[Int] = None): Route = {
    val movies = page match {
      case Some(p) => MoviesDB.findMoviesPage(p)
      case None => MoviesDB.findAllMovies
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
  }

  def getMoviesByGenre(genre: Option[String], page: Option[Int] = None): Route = {
    genre match {
      case Some(g) => {
        val movies = page match {
          case Some(p) => MoviesDB.findMovieByGenrePage(g, p)
          case None => MoviesDB.findMovieByGenre(g)
        }
        complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
      }
      case None => complete(StatusCodes.NoContent)
    }
  }

  def getMoviesByTitleOfGenre(title: Option[String], genre: Option[String], page: Option[Int] = None): Route = {
    (title, genre) match {
      case (Some(t), Some(g)) => {
        val movies = page match {
          case Some(p) => MoviesDB.findMovieByTitleAndGenrePage(t, g, p)
          case None => MoviesDB.findMovieByTitleAndGenre(t, g)
        }
        complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
      }
      case (Some(t), None) => {
        val movies = page match {
          case Some(p) => MoviesDB.findMovieByTitlePage(t, p)
          case None => MoviesDB.findMovieByTitle(t)
        }
        complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
      }
      case (None, Some(g)) => {
        val movies = page match {
          case Some(p) => MoviesDB.findMovieByGenrePage(g, p)
          case None => MoviesDB.findMovieByGenre(g)
        }
        complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
      }
      case _ => complete(StatusCodes.NoContent)
    }
  }

  def getMoviesByTitle(title: Option[String], page: Option[Int] = None): Route = {
    title match {
      case Some(t) => {
        val movies = page match {
          case Some(p) => MoviesDB.findMovieByTitlePage(t, p)
          case None => MoviesDB.findMovieByTitle(t)
        }
        complete(HttpEntity(ContentTypes.`application/json`, write(movies)))
      }
      case None => complete(StatusCodes.NoContent)
    }
  }

  def addScreening(screeningEntity: Screening): Route = {
    MoviesDB.getMovie(screeningEntity.movieid) match {
      case Some(m) => {
        val screening = ScreeningsDB.addScreening(screeningEntity)
        complete(HttpEntity(ContentTypes.`application/json`, write(screening)))
      }
      case None => complete(StatusCodes.BadRequest -> HttpEntity(ContentTypes.`application/json`, write(Error("Invalid movieid"))))
    }
  }

  def getScreening(screeningId: Int): Route = {
    ScreeningsDB.getScreeningById(screeningId) match {
      case Some(s) => complete(HttpEntity(ContentTypes.`application/json`, write(s)))
      case None => complete(StatusCodes.NotFound)
    }
  }

  def getScreenings(page: Option[Int] = None): Route = {
    val screenings = page match {
      case Some(p) => ScreeningsDB.getAllScreeningsPage(p)
      case None => ScreeningsDB.getAllScreenings
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(screenings)))
  }

  def getScreeningsByMovieId(movieId: Int, page: Option[Int] = None): Route = {
    val screenings = page match {
      case Some(p) => ScreeningsDB.getScreeningsByMovieIdPage(movieId, p)
      case None => ScreeningsDB.getScreeningsByMovieId(movieId)
    }
    complete(HttpEntity(ContentTypes.`application/json`, write(screenings)))
  }

  def getServerStatus: Route = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Server started: " + ServerStatus.serverId))
  }

  def getServerStatusJson: Route = {
    complete(HttpEntity(ContentTypes.`application/json`, write(ServerStatus.server)))
  }
}
