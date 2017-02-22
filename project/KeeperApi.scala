import sbt.Keys._
//import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.lightbend.lagom.sbt.LagomImport.lagomScaladslApi
import sbt._

object KeeperApi {
  private[this] val keeperSettings = Shared.commonSettings ++ Seq(
    name := "keeper",
    libraryDependencies ++= Seq(lagomScaladslApi)
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val keeperApi = (project in file("keeper-api"))
    .settings(keeperSettings: _*)
    .dependsOn(ServiceSecurity.security)
}