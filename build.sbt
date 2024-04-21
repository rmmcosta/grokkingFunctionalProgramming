name := "Grokking Functional Programming"

version := "0.1"

scalaVersion := "3.3.1" // or the version you are using

libraryDependencies ++= Seq(
  // Add your library dependencies here
)

unmanagedJars in Compile += file("lib/meeting-scheduler-0.0.1.jar")
