val ZioVersion    = "1.0.0-RC18-2"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

lazy val graalLocalBuild = settingKey[Boolean]("Whether to build locally or with docker")

lazy val root = (project in file("."))
  .settings(
    organization := "ZIO",
    name := "zio-awesome-project",
    version := "0.0.1",
    scalaVersion := "2.12.11",
    maxErrors := 3,
    libraryDependencies ++= Seq(
      "dev.zio"    %% "zio"         % ZioVersion
    ),
    graalLocalBuild := true,
    graalVMNativeImageGraalVersion := {
      if (graalLocalBuild.value) None
      else Some("20.0.0")
    },
    graalVMNativeImageCommand :=
      sys.env.get("GRAAL_NATIVE_IMAGE")
        .getOrElse(graalVMNativeImageCommand.value),
    graalVMNativeImageOptions ++= Seq(
      "-H:+AddAllCharsets",
      "--no-fallback",
      "-H:+ReportExceptionStackTraces",
      "-H:EnableURLProtocols=http,https"
      //"--allow-incomplete-classpath",
      //"-H:ReflectionConfigurationFiles=/opt/graalvm/stage/resources/reflection.json"
    ) ++ {
      if (scala.util.Properties.isMac)
        Seq.empty
      else
        Seq("--static")
    }
  )
    .enablePlugins(GraalVMNativeImagePlugin)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
addCompilerPlugin("io.tryp" % "splain" % "0.5.1" cross CrossVersion.patch)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
