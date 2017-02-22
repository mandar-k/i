import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object KeeperImpl {
  private[this] val keeperImplSettings = Shared.commonSettings ++ Seq(
    name := "keeperImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val keeperImpl = (project in file("keeper-impl"))
    .settings(keeperImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(Security.security, Shared.sharedJvm)
}