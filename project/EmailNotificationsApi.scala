import com.lightbend.lagom.sbt.LagomImport.lagomScaladslApi
import sbt.Keys._
import sbt._

object EmailNotificationsApi {
  private[this] val emailNotificationsSettings = Shared.commonSettings ++ Seq(
    name := "keeper",
    libraryDependencies ++= Seq(lagomScaladslApi)
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val emailNotificationsApi = (project in file("emailnotifications-api"))
    .settings(emailNotificationsSettings: _*)
    .dependsOn(ServiceSecurity.security, KeeperApi.keeperApi)
}