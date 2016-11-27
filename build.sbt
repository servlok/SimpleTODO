name := "SimpleTODOScala"

version := "0.1"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")


libraryDependencies ++= {
  val akkaV = "2.4.8"
  val reactiveMongoV = "0.12.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.reactivemongo" %% "reactivemongo" % reactiveMongoV
  )
}
