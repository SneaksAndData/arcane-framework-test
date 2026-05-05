import sbtrelease.ReleaseStateTransformations.{checkSnapshotDependencies, inquireVersions, publishArtifacts, runClean, setReleaseVersion}

val scala383 = "3.8.3"

ThisBuild / organization := "com.sneaksanddata"
ThisBuild / scalaVersion := scala383

resolvers += "Arcane framework repo" at "https://maven.pkg.github.com/SneaksAndData/arcane-framework-scala"

credentials += Credentials(
    "GitHub Package Registry",
    "maven.pkg.github.com",
    "_",
    sys.env("GITHUB_TOKEN")
)

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
    val ghToken = sys.env.get("GITHUB_TOKEN")
    ghToken.map { token =>
        "GitHub Package Registry" at s"https://maven.pkg.github.com/$ghRepo"
    }
}
publishMavenStyle := true

lazy val root = (project in file("."))
  .settings(
      name := "arcane-framework-test",
      idePackagePrefix := Some("com.sneaksanddata.arcane.framework.testkit"),

      libraryDependencies += "com.sneaksanddata" % "arcane-framework_3" % "2.2.0-7-gc684807",
      libraryDependencies += "dev.zio" %% "zio-test-sbt"      % "2.1.24",
      libraryDependencies += "dev.zio" %% "zio-test"          % "2.1.24",

      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.19" % Test,

      // Compiler options
      Test / logBuffered := false,

  )
