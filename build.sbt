name := "SimpleTODOScala"

version := "0.2-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"

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
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-contrib" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-persistence" % akkaV,
    "com.github.ironfish" %% "akka-persistence-mongo-casbah" % "0.7.6",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.reactivemongo" %% "reactivemongo" % reactiveMongoV,

    // json marshalling
    "org.json4s" %% "json4s-native" % "3.5.0",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.11.0"

  )
}
