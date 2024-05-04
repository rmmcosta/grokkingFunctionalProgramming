val scala3Version = "3.4.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "meeting-scheduler",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.junit.jupiter" % "junit-jupiter-api" % "5.8.2" % Test,
      "org.junit.jupiter" % "junit-jupiter-engine" % "5.8.2" % Test,
      "org.typelevel" %% "cats-effect" % "3.3.1"
    ),
    Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
  )
