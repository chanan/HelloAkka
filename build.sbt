name := "HelloAkka"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.0",
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "2.1.0"
)