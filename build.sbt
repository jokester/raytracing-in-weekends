import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.jokester.raytracing1weekend"
ThisBuild / organizationName := "raytracing1weekend"

lazy val root = (project in file("."))
  .settings(
    name := "raytracing-weekend",
    libraryDependencies ++= runtimeDeps ++ testDeps ++ buildDeps,
    scalacOptions ++= Seq("-Xlint"),
  )

