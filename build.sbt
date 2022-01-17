name := "chatbot"

version := "0.1"

scalaVersion := "3.0.2"

idePackagePrefix := Some("de.htwg.rs.chatbot")

libraryDependencies ++= Seq( 
"org.scalactic" %% "scalactic" % "3.2.10",
"org.scalatest" %% "scalatest" % "3.2.10" % "test",
"org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
"org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % "test"
)

// https://mvnrepository.com/artifact/org.deeplearning4j/deeplearning4j-core
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M1.1"
