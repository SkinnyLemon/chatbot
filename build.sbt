name := "chatbot"

version := "0.1"

scalaVersion := "2.13.7"

idePackagePrefix := Some("de.htwg.rs.chatbot")

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.10",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
)

val AkkaVersion = "2.6.17"
libraryDependencies += ("com.typesafe.akka" %% "akka-stream" % AkkaVersion)
    .exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion