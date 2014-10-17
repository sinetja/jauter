organization := "tv.cntt"

name         := "jauter"

version      := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

autoScalaLibrary := false

javacOptions ++= Seq("-source", "1.5", "-target", "1.5", "-Xlint:deprecation")
