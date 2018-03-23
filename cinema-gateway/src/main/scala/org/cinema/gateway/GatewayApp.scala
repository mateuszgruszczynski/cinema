package org.cinema.gateway

import akka.actor.ActorSystem
import akka.http.javadsl.server.Rejections
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

object GatewayApp extends App{
  implicit val system = ActorSystem("Proxy")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  lazy val config = DatabaseConfig.forConfig[JdbcProfile]("db")
  lazy val db = config.db

  val incomingPort = 8000
  val outgoingPort = 9000

  def isValidToken(token: String) : Boolean = {
    Thread.sleep(Random.nextInt(10)+10) // Simulate real token verification
    token == "94ab54b5-f120-4290-a4ec-587dee09206e"
  }

  val proxy = Route {
    { context =>
      val request = context.request.withUri(context.request.uri.withHost("lb.host").withPort(9000))
        .withHeaders(context.request.headers.filter(p => p.name().toLowerCase != "host"))
        .addHeader(Host.apply("lb.host", 9000))
      println(s"Forwarding ${request}")
      if (request.getUri().path().contains("internal")) {
        context.complete(StatusCodes.NotFound)
      } else if (!request.headers.exists(h => h.name() == "Authorization") && request.method != HttpMethods.OPTIONS) {
        context.reject(Rejections.authorizationFailed)
      } else if (request.method != HttpMethods.OPTIONS && !isValidToken(request.headers.find(h => h.name() == "Authorization").head.value())) {
        context.reject(Rejections.authorizationFailed)
      }
      else {
        Source.single(request)
          .via(proxyFlow(request))
          .runWith(Sink.head)
          .flatMap(context.withRequest(request).complete(_))
      }
    }
  }
  def proxyFlow(request: HttpRequest) : Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]= {
    Http()
      .outgoingConnection(request.uri.authority.host.address(), outgoingPort)
  }

  val binding = Http(system).bindAndHandle(handler = proxy, interface = "0.0.0.0", port = incomingPort)

  binding.onComplete {
    case Success(b) => println("Starring gateway at localhost:8000")
    case Failure(ex) =>
      println(ex.getMessage)
      System.exit(1)
  }
}
