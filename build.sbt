import Dependencies._

ThisBuild / organization := "mglvl"
ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.0.1-SNAPSHOT"

lazy val `coursera-notes` =
  project
    .in(file("."))
    .settings(name := "coursera-notes")
    .settings(commonSettings)
    .settings(dependencies)

lazy val commonSettings = Seq(
  addCompilerPlugin(org.augustjune.`context-applied`),
  addCompilerPlugin(org.typelevel.`kind-projector`),
  update / evictionWarningOptions := EvictionWarningOptions.empty,
  Compile / console / scalacOptions := {
    (Compile / console / scalacOptions)
      .value
      .filterNot(_.contains("wartremover"))
      .filterNot(Scalac.Lint.toSet)
      .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
  },
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    circe.`circe-generic`,
    circe.`circe-generic-extras`,
    com.beachape.enumeratum,
    com.beachape.`enumeratum-circe`,
    com.monovore.`decline-effect`,
    org.http4s.`http4s-blaze-client`,
    org.http4s.`http4s-circe`,
    org.http4s.`http4s-dsl`,
    org.typelevel.`cats-effect`,
    compilerPlugin(com.kubukoz.`better-tostring`)
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
  ).map(_ % Test)
)
