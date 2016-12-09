//import sbt.Project.projectToRef
//
//organization in ThisBuild := "livelygig"
//
//scalaVersion in ThisBuild := "2.11.8"
//
//// a special crossProject for configuring a JS/JVM/shared structure
//lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
//  .settings(
////    scalaVersion := Versions.scalaVersion,
//    libraryDependencies ++= Settings.sharedDependencies.value
//  )
//  // set up settings specific to the JS project
//  .jsConfigure(_ enablePlugins ScalaJSWeb)
//
//lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")
//
//lazy val sharedJS = shared.js.settings(name := "sharedJS")
//
//// use eliding to drop some debug code in the production build
//lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")
//
//// instantiate the JS project for SBT with some additional settings
//lazy val clientJSDeps = List("prolog_parser.js", "validator.js")
//
//lazy val client: Project = project("client")
//  .settings(
//    name := "client",
//    version := Versions.appVersion,
////    scalaVersion := Versions.scalaVersion,
//    scalacOptions ++= Settings.scalacOptions,
//    resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"),
//    libraryDependencies ++= Settings.scalajsDependencies.value,
//    // by default we do development build, no eliding
//    elideOptions := Seq(),
//    scalacOptions ++= elideOptions.value,
//    jsDependencies ++= Settings.jsDependencies.value,
//    // RuntimeDOM is needed for tests
//    jsDependencies += RuntimeDOM % "test",
//    // yes, we want to package JS dependencies
//    skip in packageJSDependencies := false,
//    jsDependencies ++= clientJSDeps.map(ProvidedJS / _),
//    // use Scala.js provided launcher code to start the client app
//    persistLauncher := true,
//    persistLauncher in Test := false
//  )
//  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
//  .dependsOn(sharedJS)
//
//
//// Client projects (just one in this case)
//lazy val clients = Seq(client)
//// instantiate the JVM project for SBT with some additional settings
//lazy val server = project("server")
//  .settings(
//    name := "server",
//    version := Versions.appVersion,
////    scalaVersion := Versions.scalaVersion,
//    scalacOptions ++= Settings.scalacOptions,
//    resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"), //add resolver
//    libraryDependencies ++= Settings.jvmDependencies.value,
////    commands += ReleaseCmd,
//    compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
//    // connect to the client project
//    scalaJSProjects := clients,
//    pipelineStages in Assets := Seq(scalaJSPipeline),
//    pipelineStages := Seq(digest, gzip),
//    includeFilter in(Assets, LessKeys.less) := "main.less",
//    // compress CSS
//    LessKeys.compress in Assets := true
//
//  )
//  .enablePlugins(PlayScala, LagomPlay)
//  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
//  .aggregate(clients.map(projectToRef): _*)
//  .dependsOn(sharedJVM)
//
//
//def project(id: String) = Project(id, base = file(id))
//  .settings(
//    scalacOptions in Compile += "-Xexperimental" // this enables Scala lambdas to be passed as Java SAMs
//  )
//  .settings(
//    libraryDependencies ++= Seq(
//      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.3" // actually, only api projects need this
//    )
//  )
//
//// do not delete database files on start
//lagomCassandraCleanOnStart in ThisBuild := false
//
//// loads the Play server project at sbt startup
//onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
//
//
//
//
//

import sbt.Project.projectToRef

organization in ThisBuild := "livelygig"

scalaVersion in ThisBuild := "2.11.8"

lazy val root = (project in file("."))
  .settings(name := "online-auction-scala")
  .aggregate(client, webGateway)
  .settings(commonSettings: _*)

// a special crossProject for configuring a JS/JVM/shared structure
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(
//    scalaVersion := Versions.scalaVersion,
    libraryDependencies ++= Settings.sharedDependencies.value
  )
  // set up settings specific to the JS project
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

// use eliding to drop some debug code in the production build
lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")

// instantiate the JS project for SBT with some additional settings
lazy val clientJSDeps = List("prolog_parser.js", "validator.js")

lazy val client: Project = (project in file("client"))
  .settings(
    name := "client",
    version := Versions.appVersion,
//    scalaVersion := Versions.scalaVersion,
    scalacOptions ++= Settings.scalacOptions,
    resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"),
    libraryDependencies ++= Seq(lagomScaladslServer),
    libraryDependencies ++= Settings.scalajsDependencies.value,
    // by default we do development build, no eliding
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    jsDependencies ++= Settings.jsDependencies.value,
    // RuntimeDOM is needed for tests
    jsDependencies += RuntimeDOM % "test",
    // yes, we want to package JS dependencies
    skip in packageJSDependencies := false,
    jsDependencies ++= clientJSDeps.map(ProvidedJS / _),
    // use Scala.js provided launcher code to start the client app
    persistLauncher := true,
    persistLauncher in Test := false
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)

// Client projects (just one in this case)
lazy val clients = Seq(client)

lazy val webGateway = (project in file("web-gateway"))
  .settings(
    name := "server",
    version := Versions.appVersion,
    //    scalaVersion := Versions.scalaVersion,
    scalacOptions ++= Settings.scalacOptions,
    resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"), //add resolver
    libraryDependencies ++= Settings.jvmDependencies.value,
    //    commands += ReleaseCmd,
    compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
    // connect to the client project
    scalaJSProjects := clients,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    includeFilter in(Assets, LessKeys.less) := "main.less",
    // compress CSS
    LessKeys.compress in Assets := true

  )
  .enablePlugins(PlayScala, LagomPlay)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJVM)

def commonSettings: Seq[Setting[_]] = Seq(
)

lagomCassandraCleanOnStart in ThisBuild := false

