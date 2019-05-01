ThisBuild / scalaVersion     := "2.12.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .aggregate(scalaParent, scala211, scala212)
  .settings(
    name := "sbt-cross-build-dependson-example",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    crossScalaVersions := Seq() // https://github.com/sbt/sbt/issues/4262#issuecomment-405607763
  )

lazy val scalaParent = (project in file("scala-parent")).settings(crossScalaVersions := Seq("2.11.0", "2.12.0"))
lazy val scala211  = (project in file("scala-2.11")).dependsOn(scalaParent).settings(scalaVersion := "2.11.0")
lazy val scala212  = (project in file("scala-2.12")).dependsOn(scalaParent).settings(scalaVersion := "2.12.0")

