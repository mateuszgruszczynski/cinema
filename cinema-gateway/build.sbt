import sbt.Keys._

name := "cinema-gateway"

version := "0.1"

scalaVersion := "2.12.4"

assemblyOutputPath in assembly := new File("jar/cinema-gateway.jar")
assemblyJarName in assembly := "cinema-gateway.jar"
mainClass in assembly := Some("org.cinema.gateway.GatewayApp")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.9",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.pauldijou" %% "jwt-core" % "0.12.1",
  "com.typesafe.slick" %% "slick" % "3.2.1"
)

// TODO find way how to make it in command line
test in assembly := {}