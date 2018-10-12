name := "macrosdemo2"

version := "0.1"

scalaVersion := "2.12.7"

lazy val macros: Project = (project in file("macros")).settings(
  libraryDependencies ++= Seq(
    scalaOrganization.value % "scala-reflect" % scalaVersion.value,
    scalaOrganization.value % "scala-compiler" % scalaVersion.value,
//    "com.chuusai" %% "shapeless" % "2.3.3"
  )
)

lazy val core: Project = (project in file("core")).aggregate(macros).dependsOn(macros)