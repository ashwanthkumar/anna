import sbt._
import sbt.Keys._
import scala.collection.JavaConversions._

object Build extends Build {
  val AppVersion = System.getenv().getOrElse("GO_PIPELINE_LABEL", "1.0.0-SNAPSHOT")
  val ScalaVersion = "2.10.4"

  val main = Project("anna", file("."))
    .settings(organization := "in.ashwanthkumar",
      version := AppVersion,
      libraryDependencies ++= appDependencies
    )

  lazy val appDependencies = Seq(
    "org.scalatest" %% "scalatest" % "2.2.0" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test"
  )

  override val settings = super.settings ++ Seq(
    fork in run := false,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    parallelExecution in This := false,
    publishMavenStyle := true,
    crossPaths := true,
    publishArtifact in Test := false,
    publishArtifact in(Compile, packageDoc) := false,
    // publishing the main sources jar
    publishArtifact in(Compile, packageSrc) := true,
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
}
