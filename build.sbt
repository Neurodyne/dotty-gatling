val dottyVersion    = "0.22.0-bin-20191225-2e3fbfd-NIGHTLY"
val scala212Version = "2.12.10"
val zioVersion      = "1.0.0-RC17"

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

enablePlugins(GatlingPlugin)

lazy val zioDeps = libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-test"     % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
)

lazy val catsDeps = libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"   % "2.1.0-RC1",
  "org.typelevel" %% "cats-effect" % "2.0.0"
)

lazy val gatlingDeps = libraryDependencies ++= Seq(
"io.gatling.highcharts" % "gatling-charts-highcharts" % "3.3.1" % "test,it",
"io.gatling"            % "gatling-test-framework"    % "3.3.1" % "test,it"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-gatling",
    version := "0.0.1",
    scalaVersion := dottyVersion,
    crossScalaVersions := Seq(dottyVersion, scala212Version),
    scalacOptions ++= Seq(
      "-noindent",
      "-language:Scala2Compat",
      "-language:implicitConversions", 
      "-language:postfixOps"
    ),
    zioDeps,
    catsDeps,
    gatlingDeps,
    libraryDependencies := libraryDependencies.value.map(_.withSources.withDottyCompat(scalaVersion.value).exclude("org.scala-lang.modules", "scala-java8-compat_2.12")),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")


addCommandAlias("sim", "gatling:testOnly computerdatabase.BasicSimulation")
