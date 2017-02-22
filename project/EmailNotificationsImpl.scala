import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object EmailNotificationsImpl {
  private[this] val emailNotificationsImplSettings = Shared.commonSettings ++ Seq(
    name := "emailNotificationsImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val emailNotificationsImpl = (project in file("emailnotifications-impl"))
    .settings(emailNotificationsImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, EmailNotificationsApi.emailNotificationsApi)
}