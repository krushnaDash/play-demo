import scala.Seq

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-scala-hello-world-tutorial""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.8",

    libraryDependencies ++= {
      val akkaVersion = "2.6.19"
      val akkaHttpVersion = "10.2.9"
      Seq(
        guice,
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
      )
    },
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
