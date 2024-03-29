import com.typesafe.sbt.packager.archetypes.{JavaServerAppPackaging}



name := "customertransactions"

version := "1.0"

description := "GraphQL server with akka-http and sangria"

scalaVersion := "2.12.3"


lazy val root = (project in file(".")).
  enablePlugins(JavaServerAppPackaging).
  settings(
    name := "Scala.js Tutorial",
    scalaVersion := "2.12.3",
    version := "1.0"
  )



scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "1.3.0",
  "org.sangria-graphql" %% "sangria-spray-json" % "1.0.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",

  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.6",

  "org.json4s" %% "json4s-native" % "3.6.0", //json4s support for working with json serialization/deserialization
  "org.json4s" %% "json4s-jackson" % "3.6.0",
  "org.dispatchhttp" % "dispatch-core_2.12" % "1.0.0-M1",
  "com.typesafe" % "config" % "1.3.2",


  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

Revolver.settings
