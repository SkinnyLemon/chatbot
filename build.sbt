name := "chatbot"

version := "0.1"

scalaVersion := "3.1.1-RC1"

idePackagePrefix := Some("de.htwg.rs.chatbot")

libraryDependencies ++= Seq(
  ("io.github.vincenzobaz" %% "spark-scala3" % "0.1.3").exclude("org.scala-lang.modules", "scala-parser-combinators_2.13").exclude("org.scala-lang.modules", "scala-xml_2.13"),
  "org.scalactic" %% "scalactic" % "3.2.10",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
  "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
)

val AkkaVersion = "2.6.18"
libraryDependencies += ("com.typesafe.akka" %% "akka-stream" % AkkaVersion)
    .exclude("org.scala-lang.modules", "scala-parser-combinators_2.13")

libraryDependencies += ("org.apache.kafka" %% "kafka" % "3.0.0").cross(CrossVersion.for3Use2_13)
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion

val sparkVersion = "3.2.0"

libraryDependencies += ("org.apache.spark" %% "spark-core" % sparkVersion).cross(CrossVersion.for3Use2_13).exclude("org.scala-lang.modules", "scala-parser-combinators_2.13").exclude("org.scala-lang.modules", "scala-xml_2.13")
libraryDependencies += ("org.apache.spark" %% "spark-streaming" % sparkVersion).cross(CrossVersion.for3Use2_13).exclude("org.scala-lang.modules", "scala-xml_2.13")
libraryDependencies += ("org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion).cross(CrossVersion.for3Use2_13)
libraryDependencies += ("org.apache.spark" %% "spark-sql" % sparkVersion).cross(CrossVersion.for3Use2_13).exclude("org.scala-lang.modules", "scala-parser-combinators_2.13").exclude("org.scala-lang.modules", "scala-xml_2.13")
