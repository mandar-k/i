import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object ContentImpl {
  private[this] val contentImplSettings = Shared.commonSettings ++ Seq(
    name := "contentImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val contentImpl = (project in file("content-impl"))
    .settings(contentImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(Security.security, Shared.sharedJvm)
}