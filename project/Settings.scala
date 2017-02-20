import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslApi, lagomScaladslKafkaBroker, lagomScaladslPersistenceCassandra}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import play.sbt.PlayImport.{cache, filters, specs2, ws}
import sbt._

/**
  * Application settings. Configure the build for your application here.
  * You normally don't have to touch the actual build definition after this.
  */
object Settings {
  /** The name of your application */
  val name = "livelygig"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  /** Set some basic options when running the project with Revolver */
  val jvmRuntimeOptions = Seq(
    "-Xmx1G"
  )

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
  val sharedDependencies = Def.setting(Seq(
    "com.chuusai" %%% "shapeless" % "2.3.2",
    "com.typesafe.play" %%% "play-json" % "2.6.0-M3"
  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(Seq(
    "com.vmunier" %% "scalajs-scripts" % Versions.scalajsScripts,
    "org.webjars" % "font-awesome" % Versions.fontAwesome % Provided,
    "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0",
    "com.mohiva" %% "play-silhouette" % "4.0.0",
    "com.mohiva" %% "play-silhouette-persistence" % "4.0.0",
    "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0",
    "org.webjars" %% "webjars-play" % "2.5.0-2",
    "com.iheart" %% "ficus" % "1.4.0",
    "com.adrianhurt" % "play-bootstrap_2.11" % "1.1-P25-B3",
    "com.mohiva" %% "play-silhouette-testkit" % "4.0.0" % "test",
    "com.softwaremill.macwire" %% "macros" % "2.2.5",
    "com.softwaremill.macwire" %% "runtime" % "1.0.7",
    specs2 % Test,
    cache,
    filters
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % Versions.scalajsReact,
    "com.github.japgolly.scalajs-react" %%% "extra" % Versions.scalajsReact,
    "com.github.japgolly.scalacss" %%% "ext-react" % Versions.scalaCSS,
    "org.scala-js" %%% "scalajs-dom" % Versions.scalaDom,
    "io.suzaku" %%% "diode" % "1.1.1",
    "io.suzaku" %%% "diode-react" % "1.1.1",
    "org.querki" %%% "jquery-facade" % Versions.jqueryFacade,
    "org.querki" %%% "querki-jsext" % Versions.querkiJsext,
    "org.querki" %%% "bootstrap-datepicker-facade" % Versions.datePickerFacade,
    "org.denigma" %%% "selectize-facade" % Versions.selectizeFacade,
    "io.github.widok" %%% "scala-js-momentjs" % Versions.momentJSFacade
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars.npm" % "react" % Versions.react / "react-with-addons.js" commonJSName "React" minified "react-with-addons.min.js",
    "org.webjars.npm" % "react-dom" % Versions.react / "react-dom.js" commonJSName "ReactDOM" minified "react-dom.min.js" dependsOn "react-with-addons.js",
    "org.webjars.npm" % "react-dom" % Versions.react / "react-dom-server.js" commonJSName "ReactDOMServer",
    "org.webjars" % "jquery" % Versions.jQuery / "jquery.js" minified "jquery.min.js",
    "org.webjars" % "bootstrap" % Versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js",
    "org.webjars" % "log4javascript" % Versions.log4js / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js" dependsOn "jquery.js",
    "org.webjars" % "selectize.js" % Versions.selectizejs / "js/standalone/selectize.js" minified "js/standalone/selectize.min.js" dependsOn "jquery.js",
    "org.webjars" % "bootstrap-datepicker" % "1.6.1" / "bootstrap-datepicker.js" minified "bootstrap-datepicker.min.js" dependsOn "bootstrap.js"
  ))

  val apiDependencies = Def.setting(Seq(
    lagomScaladslApi,
    "org.julienrf" %% "play-json-derived-codecs" % "3.3"
  ))

  /*Dependency common to all api service implementation*/
  val apiImplDependencies = Def.setting(Seq(
    lagomScaladslPersistenceCassandra,
    lagomScaladslKafkaBroker,
    "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided",
    "org.scalatest" %%% "scalatest" % Versions.scalaTest % "test",
    "org.julienrf" %% "play-json-derived-codecs" % "3.3"
  ))

}
