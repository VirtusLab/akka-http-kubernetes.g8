val akkaVersion = "$akka_version$"
val akkaHttpVersion = "$akka_http_version$"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin, JavaServerAppPackaging)
  .settings(
    name := "$normalized_name$",
    organization := "$organization$",
    scalaVersion := "$scala_version$",
    dockerBaseImage := "openjdk:8-jre-slim",
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-circe" % "1.32.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.github.pureconfig" %% "pureconfig" % "0.12.3",
      "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "io.circe" %% "circe-generic" % "0.13.0",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
