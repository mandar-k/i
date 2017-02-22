import com.lightbend.lagom.sbt.LagomImport.lagomScaladslApi
import sbt.Keys._
import sbt._

object EmailNotificationsApi {
  private[this] val emailNotificationSettings = Shared.commonSettings ++ Seq(
    name := "keeper",
    libraryDependencies ++= Seq(lagomScaladslApi)
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val emailNotificationApi = (project in file("emailnotification-api"))
    .settings(emailNotificationSettings: _*)
    .dependsOn(Security.security)
}