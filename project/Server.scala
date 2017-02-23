import com.lightbend.lagom.sbt.LagomImport.lagomScaladslServer
import com.lightbend.lagom.sbt.LagomPlay
//import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.digest.Import._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.less.Import._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.debian.DebianPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.jdkpackager.JDKPackagerPlugin
import com.typesafe.sbt.packager.linux.LinuxPlugin
import com.typesafe.sbt.packager.rpm.RpmPlugin
import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.packager.windows.WindowsPlugin
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.PlayKeys
import play.sbt.PlayImport.PlayKeys._
import play.sbt.PlayLayoutPlugin
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import webscalajs.WebScalaJS.autoImport._

import webscalajs.WebScalaJS.autoImport.{scalaJSPipeline, devCommands, scalaJSProjects}

object Server {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      Cache.ehCache, Akka.actor, Akka.logging, Play.playFilters, Play.playWs, Play.playWebjars, Play.playBootstrap,
      Authentication.silhouette, Authentication.hasher, Authentication.persistence, Authentication.crypto,
      WebJars.fontAwesome, WebJars.materialize, WebJars.moment, WebJars.mousetrap,
      Utils.crypto, Utils.commonsIo, SharedDependencies.macwire, Akka.testkit, Play.playTest,
      SharedDependencies.scalaTest, lagomScaladslServer, Play.scalajsScripts
    )
  }

  private[this] lazy val serverSettings = Shared.commonSettings ++ Seq(
    name := "WebGateway",
    maintainer := "Livelygig Admin <admin@livelygig.com>",
    description := "Livelygig WebGateway",

    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= dependencies,
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    devCommands in scalaJSPipeline += "runAll",
    // connect to the client project
    scalaJSProjects := Seq(Client.client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    routesGenerator := InjectedRoutesGenerator,
    externalizeResources := false,

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    LessKeys.compress in Assets := true,

    // Fat-Jar Assembly
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value),
    mainClass in assembly := Some(Shared.projectName)

    // Code Quality
    //    scapegoatIgnoredFiles := Seq(".*/Row.scala", ".*/Routes.scala", ".*/ReverseRoutes.scala", ".*/JavaScriptReverseRoutes.scala", ".*/*.template.scala")
  )

  lazy val webGateway = (project in file("web-gateway"))
    .enablePlugins(
      SbtWeb, play.sbt.PlayScala, JavaAppPackaging,
      UniversalPlugin, LinuxPlugin, DebianPlugin, RpmPlugin, DockerPlugin, WindowsPlugin, JDKPackagerPlugin, LagomPlay
    )
    .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
    .dependsOn(UserProfileApi.userProfileApi, ContentApi.contentApi,
    ServiceSecurity.security, EmailNotificationsApi.emailNotificationsApi, KeeperApi.keeperApi, ConnectionsApi.connectionsApi,
    Shared.sharedJvm)
    .settings(serverSettings: _*)
    .settings(Packaging.settings: _*)

  //    Shared.withProjects(ret, Seq(Shared.sharedJvm, Utilities.metrics))

}