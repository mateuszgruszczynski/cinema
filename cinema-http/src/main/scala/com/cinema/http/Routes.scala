package com.cinema.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import com.cinema.http.model._
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.headers._

import scala.util.Random

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat10(User)
  implicit val authFormat = jsonFormat2(Authenticate)
  implicit val walletFormat = jsonFormat3(Wallet)
  implicit val movieFormat = jsonFormat8(Movie)
  implicit val orderFormat = jsonFormat6(Order)
  implicit val screeningFormat = jsonFormat8(Screening)
}

object Routes extends Directives with JsonSupport{

  val api = ignoreTrailingSlash {
    respondWithHeaders(`Access-Control-Allow-Origin`(HttpOriginRange.*), `Access-Control-Allow-Headers`("http-auth-token", "content-type", "authorization")) {
      options {
        complete(s"Supported methods : GET, POST.")
      } ~ extractRequestContext { ctx =>
        path("") {
          get {
            RoutesHandler.getServerStatusJson
          }
        } ~ path("health") {
          get {
            RoutesHandler.getServerStatus
          }
        } ~ path("users") {
          parameter('page.as[Int].?) { page =>
            get {
              RoutesHandler.getAllUsers(page)
            } ~ post {
              entity(as[User]) { user =>
                RoutesHandler.addUser(user)
              }
            }
          }
        } ~ path("users" / IntNumber) { id =>
          get {
            Security.checkSession(id, ctx, RoutesHandler.getUserById(id))
          } ~ (post | put) {
            entity(as[User]) { user =>
              Security.checkSession(id, ctx, RoutesHandler.updateUser(id, user))
            }
          }
        } ~ path("users" / IntNumber / "wallet") { id =>
          get {
            Security.checkSession(id, ctx, RoutesHandler.getUserWallet(id))
          }
        } ~ path("users" / IntNumber / "wallet" / "payin") { id =>
          parameters('value.as[Float]) { value =>
            get {
              Security.checkSession(id, ctx, RoutesHandler.increaseUserWallet(id, BigDecimal(value)))
            }
          }
        } ~ path("users" / IntNumber / "orders") { id =>
          get {
            parameter('page.as[Int].?) { page => Security.checkSession(id, ctx, RoutesHandler.getUserOrders(id, page)) }
          } ~ post {
            entity(as[Order]) { order =>
              Security.checkSession(order.userid, ctx, RoutesHandler.placeOrder(order))
            }
          }
        } ~ path("users" / IntNumber / "orders" / IntNumber) { (userid, orderid) =>
          get {
            Security.checkSession(userid, ctx, RoutesHandler.getUserOrder(userid, orderid))
          }
        } ~ path("users" / IntNumber / "orders" / IntNumber / "pay") { (userid, orderid) =>
          get {
            Security.checkSession(userid, ctx, RoutesHandler.payOrder(orderid))
          }
        } ~ path("authenticate") {
          post {
            entity(as[Authenticate]) { a =>
              RoutesHandler.authenticate(a)
            }
          }
        } ~ path("orders") {
          get {
            parameter('page.as[Int].?) { page => RoutesHandler.getAllOrders(page) }
          }
        } ~ path("orders" / IntNumber) { id =>
          get {
            RoutesHandler.getOrder(id)
          }
        } ~ path("movies") {
          get {
            parameter('page.as[Int].?) { page => RoutesHandler.getMovies(page) }
          } ~ post {
            entity(as[Movie]) { movie =>
              RoutesHandler.addMovie(movie)
            }
          }
        } ~ path("movies" / IntNumber) { id =>
          get {
            RoutesHandler.getMovie(id)
          }
        } ~ path("movies" / IntNumber / "screenings") { id =>
          get {
            parameter('page.as[Int].?) { page => RoutesHandler.getScreeningsByMovieId(id, page) }
          }
        } ~ path("movies" / "search") {
          parameters('title.?, 'genre.?, 'page.as[Int].?) { (title, genre, page) =>
            get {
              RoutesHandler.getMoviesByTitleOfGenre(title, genre, page)
            }
          }
        } ~ path("screenings") {
          get {
            parameter('page.as[Int].?) { page => RoutesHandler.getScreenings(page) }
          } ~ post {
            entity(as[Screening]) { screening =>
              RoutesHandler.addScreening(screening)
            }
          }
        }
      }
    }
  }
}