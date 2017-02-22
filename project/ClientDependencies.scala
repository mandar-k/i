import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

/**
  * Application settings. Configure the build for your application here.
  * You normally don't have to touch the actual build definition after this.
  */
object ClientDependencies {

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % "0.11.3",
    "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.1",
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "io.suzaku" %%% "diode" % "1.1.1",
    "io.suzaku" %%% "diode-react" % "1.1.1",
    "org.querki" %%% "jquery-facade" % "1.0",
    "org.querki" %%% "querki-jsext" % "0.8",
    "org.querki" %%% "bootstrap-datepicker-facade" % "0.8",
    "ru.pavkin" %%% "scala-js-momentjs" % "0.5.1"
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars.npm" % "react" % "15.4.2" / "react-with-addons.js" commonJSName "React" minified "react-with-addons.min.js",
    "org.webjars.npm" % "react-dom" % "15.4.2" / "react-dom.js" commonJSName "ReactDOM" minified "react-dom.min.js" dependsOn "react-with-addons.js",
    "org.webjars" % "jquery" % "2.2.4" / "jquery.js" minified "jquery.min.js",
    "org.webjars" % "bootstrap" % "3.3.7" / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js",
    "org.webjars" % "log4javascript" % "1.4.13-1" / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js" dependsOn "jquery.js",
    "org.webjars" % "selectize.js" % "0.12.4" / "js/standalone/selectize.js" minified "js/standalone/selectize.min.js" dependsOn "jquery.js",
    "org.webjars" % "bootstrap-datepicker" % "1.6.4" / "bootstrap-datepicker.js" minified "bootstrap-datepicker.min.js" dependsOn "bootstrap.js"
  ))

}
