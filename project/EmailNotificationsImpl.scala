import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object EmailNotificationsImpl {
  private[this] val emailNotificationImplSettings = Shared.commonSettings ++ Seq(
    name := "emailNotificationImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val emailNotificationImpl = (project in file("emailnotification-impl"))
    .settings(emailNotificationImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(Security.security, Shared.sharedJvm)
}