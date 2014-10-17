organization := "tv.cntt"

name         := "jauter"

version      := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

autoScalaLibrary := false

javacOptions in (Compile) ++= Seq("-source", "1.5", "-target", "1.5", "-Xlint:deprecation")

javacOptions in (Compile, doc) := Seq("-source", "1.5")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"
