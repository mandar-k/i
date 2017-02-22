import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslApi, lagomScaladslServer}
import sbt.Keys.libraryDependencies
import sbt._

object ServiceSecurity {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      SharedDependencies.derivedCodecs,SharedDependencies.scalaTest
    )
  }

  private[this] lazy val securitySettings = Shared.commonSettings ++ Seq(
    libraryDependencies ++= dependencies
  )

  lazy val security = (project in file("security"))
    .settings(securitySettings: _*)

}