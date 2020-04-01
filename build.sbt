val ZioVersion    = "1.0.0-RC18-2"
val Specs2Version = "4.7.0"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    organization := "ZIO",
    name := "zio-awesome-project",
    version := "0.0.1",
    scalaVersion := "2.12.11",
    maxErrors := 3,
    libraryDependencies ++= Seq(
      "dev.zio"    %% "zio"         % ZioVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test"
    ),
    graalVMNativeImageGraalVersion := Some("20.0.0"),
    graalVMNativeImageOptions ++= Seq(
      "--static",
      "-H:+AddAllCharsets",
      "--no-fallback",
      //"--allow-incomplete-classpath",
      "-H:+ReportExceptionStackTraces",
      "-H:EnableURLProtocols=http,https"
      //"-H:ReflectionConfigurationFiles=/opt/graalvm/stage/resources/reflection.json"
    )
  )
    .enablePlugins(GraalVMNativeImagePlugin)

// Refine scalac params from tpolecat
scalacOptions --= Seq(
  "-Xfatal-warnings"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")