package com.cinema.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

object CinemaApp extends App with LazyLogging{

  implicit val actorSystem: ActorSystem = ActorSystem.create("cinema")
  implicit val materializer: ActorMaterializer = ActorMaterializer.create(actorSystem)
  implicit val executionContext = actorSystem.dispatcher

  val bindingFuture = Http().bindAndHandle(Routes.api, "0.0.0.0", 9000)

  bindingFuture.onComplete {
    case Success(b) => println("Starring server at localhost:9000")
    case Failure(ex) =>
      println(ex.getMessage)
      System.exit(1)
  }
}