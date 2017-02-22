import com.lightbend.lagom.sbt.LagomImport.lagomScaladslApi
import sbt.Keys._
import sbt._

object UserProfileApi {
  private[this] val userProfileSettings = Shared.commonSettings ++ Seq(
    name := "keeper",
    libraryDependencies ++= Seq(lagomScaladslApi)
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val userProfileApi = (project in file("userprofile-api"))
    .settings(userProfileSettings: _*)
    .dependsOn(ServiceSecurity.security, KeeperApi.keeperApi)
}