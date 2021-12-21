name := "chatbot"

version := "0.1"

scalaVersion := "3.1.1-RC1"

idePackagePrefix := Some("de.htwg.rs.chatbot")

val ScalaTestVersion = "3.2.10"
val AkkaVersion = "2.6.18"
val DeepLearning4jVersion = "1.0.0-M1.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
  ("org.apache.kafka" %% "kafka" % "3.0.0")
    .cross(CrossVersion.for3Use2_13),

  "org.scalactic" %% "scalactic" % ScalaTestVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % "test",

  ("com.typesafe.akka" %% "akka-stream" % AkkaVersion)
    .exclude("org.scala-lang.modules", "scala-parser-combinators_2.13"),
  ("com.typesafe.akka" %% "akka-actor" % AkkaVersion)
    .exclude("org.scala-lang.modules", "scala-parser-combinators_2.13"),

  "org.deeplearning4j" % "deeplearning4j-core" % DeepLearning4jVersion,
  "org.nd4j" % "nd4j-api" % DeepLearning4jVersion,
  "org.datavec" % "datavec-api" % DeepLearning4jVersion,
  "org.nd4j" % "nd4j-native" % DeepLearning4jVersion,
  "org.slf4j" % "slf4j-simple" % "1.6.2" % Test
)
