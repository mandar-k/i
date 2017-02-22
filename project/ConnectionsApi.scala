import com.lightbend.lagom.sbt.LagomImport.lagomScaladslApi
import sbt.Keys._
import sbt._

object ConnectionsApi {
  private[this] val connectionsSettings = Shared.commonSettings ++ Seq(
    name := "keeper",
    libraryDependencies ++= Seq(lagomScaladslApi)
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val connectionsApi = (project in file("connections-api"))
    .settings(connectionsSettings: _*)
    .dependsOn(Security.security)
}