import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslKafkaClient, lagomScaladslPersistenceCassandra}
import com.lightbend.lagom.sbt.LagomScala
import sbt.Keys._
import sbt._

object UserProfileImpl {

  private[this] val dependencies = {
    Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaClient
    )
  }

  private[this] val userProfileImplSettings = Shared.commonSettings ++ Seq(
    name := "userProfileImpl",
    libraryDependencies ++= dependencies
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val userProfileImpl = (project in file("userProfile-impl"))
    .settings(userProfileImplSettings: _*)
    .enablePlugins(LagomScala)
    .dependsOn(ServiceSecurity.security,Shared.sharedJvm, UserProfileApi.userProfileApi)
}