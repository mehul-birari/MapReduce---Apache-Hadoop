name := "MB_hw2"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.8.0-beta2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.3.0-alpha4"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "0.23.1"

libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" % "2.9.1" % "provided"

libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-shuffle" % "2.9.1"

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.2.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-engine" % "5.0.0" % Test
