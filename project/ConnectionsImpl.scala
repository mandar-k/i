import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object ConnectionsImpl {
  private[this] val connectionsImplSettings = Shared.commonSettings ++ Seq(
    name := "connectionsImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val connectionsImpl = (project in file("connections-impl"))
    .settings(connectionsImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, ConnectionsApi.connectionsApi)
}