name := "chatbot"

version := "0.1"

scalaVersion := "3.0.2"

idePackagePrefix := Some("de.htwg.rs.chatbot")

libraryDependencies ++= Seq( 
"org.scalactic" %% "scalactic" % "3.2.10",
"org.scalatest" %% "scalatest" % "3.2.10" % "test",
"org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
)
