import sbtrelease.ReleaseStateTransformations.{checkSnapshotDependencies, inquireVersions, publishArtifacts, runClean, setReleaseVersion}

val scala3 = "3.9.0"

ThisBuild / organization := "com.sneaksanddata"
ThisBuild / scalaVersion := scala3

resolvers += "Arcane framework repo" at "https://maven.pkg.github.com/SneaksAndData/arcane-framework-scala"

val githubToken: Option[String] =
  sys.env.get("GITHUB_TOKEN").orElse {
    val tokenFile = file(".ghtoken")
    if (tokenFile.exists()) Some(scala.io.Source.fromFile(tokenFile).mkString.trim) else None
  }

credentials ++= githubToken.map { token =>
  Credentials(
    "GitHub Package Registry",
    "maven.pkg.github.com",
    "_",
    token
  )
}.toSeq

releaseVersionFile := file("version.sbt")
releaseVersionBump := sbtrelease.Version.Bump.Bugfix
releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,              // : ReleaseStep
    inquireVersions,                        // : ReleaseStep
    runClean,                               // : ReleaseStep
    setReleaseVersion,                      // : ReleaseStep
    publishArtifacts,                       // : ReleaseStep
)
releaseIgnoreUntrackedFiles := true
publishTo := {
    val ghRepo = "SneaksAndData/arcane-framework-test"
    val ghUser = "_"
    githubToken.map { token =>
        "GitHub Package Registry" at s"https://maven.pkg.github.com/$ghRepo"
    }
}
publishMavenStyle := true

lazy val root = (project in file("."))
  .settings(
      name := "arcane-framework-test",
      idePackagePrefix := Some("com.sneaksanddata.arcane.framework.testkit"),

      libraryDependencies += "com.sneaksanddata" % "arcane-framework_3" % "2.3.2",
      libraryDependencies += "dev.zio" %% "zio-test-sbt"      % "2.1.27",
      libraryDependencies += "dev.zio" %% "zio-test"          % "2.1.27",

      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test,
      libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.20" % Test,

      // Compiler options
      Test / logBuffered := false,

  )
