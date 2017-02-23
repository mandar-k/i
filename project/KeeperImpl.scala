import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._
import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslPersistenceCassandra,lagomScaladslKafkaBroker,lagomScaladslPubSub}

object KeeperImpl {

  private[this] val dependencies = {
    Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker
    )
  }
  private[this] val keeperImplSettings = Shared.commonSettings ++ Seq(
    name := "keeperImpl",
    libraryDependencies ++= dependencies
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val keeperImpl = (project in file("keeper-impl"))
    .settings(keeperImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, KeeperApi.keeperApi)
}