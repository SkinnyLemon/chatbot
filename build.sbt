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

libraryDependencies += "org.apache.kafka" %% "kafka" % "3.0.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion

libraryDependencies += ("org.apache.spark" % "spark-core_2.13" % "3.2.0").exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")
libraryDependencies += ("org.apache.spark" % "spark-streaming_2.13" % "3.2.0").exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")
libraryDependencies += ("org.apache.spark" % "spark-streaming-kafka-0-10_2.13" % "3.2.0").exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")
libraryDependencies += ("org.apache.spark" % "spark-sql_2.13" % "3.2.0").exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")
