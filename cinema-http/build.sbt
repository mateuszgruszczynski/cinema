import sbt.Keys._
import sbtassembly.MergeStrategy

name := "cinema-http"

version := "0.1"

scalaVersion := "2.12.4"

assemblyOutputPath in assembly := new File("jar/cinema-http.jar")
assemblyJarName in assembly := "cinema-server.jar"
mainClass in assembly := Some("com.cinema.http.CinemaApp")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.11" exclude("org.slf4j", "slf4j-log4j12"),
  "com.typesafe.akka" %% "akka-stream" % "2.5.9" exclude("org.slf4j", "slf4j-log4j12"),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.14",
  "org.scalikejdbc" %% "scalikejdbc" % "3.2.1",
  "mysql" % "mysql-connector-java" % "6.0.6",
  //json4s
  "org.json4s" % "json4s-jackson_2.12" % "3.5.3",
  "org.json4s" %% "json4s-ext" % "3.5.3"
)

val defaultMergeStrategy: String => MergeStrategy = {
  case x if Assembly.isConfigFile(x) =>
    MergeStrategy.concat
  case PathList(ps@_*) if Assembly.isReadme(ps.last)
    || Assembly.isLicenseFile(ps.last)
    || ps.last.startsWith("LICENSE")
    || ps.last.startsWith("ASL") =>
    MergeStrategy.rename
  case PathList("META-INF", xs@_*) =>
    xs map {
      _.toLowerCase
    } match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps@(_ :: _) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: _ =>
        MergeStrategy.discard
      case "services" :: _ =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines

      case _ => MergeStrategy.last
    }
  case _ => MergeStrategy.last
}
assemblyMergeStrategy in assembly := defaultMergeStrategy

// TODO find way how to make it in command line
test in assembly := {}