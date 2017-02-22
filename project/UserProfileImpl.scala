import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object UserProfileImpl {
  private[this] val userProfileImplSettings = Shared.commonSettings ++ Seq(
    name := "userProfileImpl"
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val userProfileImpl = (project in file("userProfile-impl"))
    .settings(userProfileImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(Security.security,Shared.sharedJvm)
}