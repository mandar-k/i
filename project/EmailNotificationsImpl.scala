import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslKafkaClient, lagomScaladslPersistenceCassandra}
import com.lightbend.lagom.sbt.LagomScala
import play.twirl.sbt.SbtTwirl
import sbt.Keys._
import sbt._

object EmailNotificationsImpl {

  private[this] val dependencies = {
    import Dependencies._
    Seq(lagomScaladslPersistenceCassandra, lagomScaladslKafkaClient,Play.playMailer )
  }

  private[this] val emailNotificationsImplSettings = Shared.commonSettings ++ Seq(
    name := "emailNotificationsImpl",
    libraryDependencies ++= dependencies
    //    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val emailNotificationsImpl = (project in file("emailnotifications-impl"))
    .settings(emailNotificationsImplSettings: _*)
    .enablePlugins(LagomScala && SbtTwirl)
    .dependsOn(ServiceSecurity.security, Shared.sharedJvm, EmailNotificationsApi.emailNotificationsApi)
}