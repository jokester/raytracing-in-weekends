import sbt._

object Dependencies {
  lazy val runtimeDeps: Seq[ModuleID] = Seq(
    // logging
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3", // this provides SLJ4J backend
  )

  lazy val testDeps: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.1.1"
  ).map(_ % Test)

  lazy val buildDeps: Seq[ModuleID] = Seq.empty
}
