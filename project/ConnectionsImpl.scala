import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslKafkaClient, lagomScaladslPersistenceCassandra}
import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object ConnectionsImpl {

  private[this] val dependencies = {
    Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaClient
    )
  }

  private[this] val connectionsImplSettings = Shared.commonSettings ++ Seq(
    name := "connectionsImpl",
    libraryDependencies ++= dependencies
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val connectionsImpl = (project in file("connections-impl"))
    .settings(connectionsImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, ConnectionsApi.connectionsApi)
}