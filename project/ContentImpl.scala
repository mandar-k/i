import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslKafkaClient, lagomScaladslPersistenceCassandra, lagomScaladslPubSub}
import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object ContentImpl {

  private[this] val dependencies = {
    Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaClient,
      lagomScaladslPubSub
    )
  }

  private[this] val contentImplSettings = Shared.commonSettings ++ Seq(
    name := "contentImpl",
    libraryDependencies ++= dependencies
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val contentImpl = (project in file("content-impl"))
    .settings(contentImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, ContentApi.contentApi, KeeperApi.keeperApi)
}