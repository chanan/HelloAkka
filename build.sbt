name := "HelloAkka"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.2"
)
