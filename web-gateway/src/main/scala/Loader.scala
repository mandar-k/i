
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.livelygig.product.connections.api.ConnectionsService
import com.livelygig.product.content.api.ContentService
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.userprofile.api.UserProfileService
import com.mohiva.play.silhouette.api.{Silhouette, SilhouetteProvider}
import silhouetteservices.SilhouetteIdentityService
import play.api.i18n.I18nComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import controllers.api.v1.auth._
import controllers.api.v1._
import controllers.terms.TermsController

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire
import controllers.{Assets, ViewController, WebJarAssets}
import modules.SilhouetteModule
import play.api.ApplicationLoader.Context
import router.Routes

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
    with I18nComponents
    with SilhouetteModule
    with LagomServiceClientComponents {

  lazy val silhouetteIdentityService = wire[SilhouetteIdentityService]

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher

  lazy val routerOption = None
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }
  // assets
  lazy val assets: Assets = wire[Assets]
  lazy val webjarAssets: WebJarAssets = wire[WebJarAssets]

  // services
  lazy val messageServiceImpl = serviceClient.implement[ContentService]
  lazy val emailNotificationImpl = serviceClient.implement[EmailNotificationsService]
  lazy val keeperService = serviceClient.implement[KeeperService]
  lazy val userProfileService = serviceClient.implement[UserProfileService]
  lazy val connectionService = serviceClient.implement[ConnectionsService]

  // controllers
  lazy val authController: AuthController = wire[AuthController]
  lazy val registrationController: RegistrationController = wire[RegistrationController]
  lazy val termsController: TermsController = wire[TermsController]
  lazy val viewController: ViewController = wire[ViewController]
  lazy val userController: UserController = wire[UserController]
  lazy val mainController: MainController = wire[MainController]

  // split route
  lazy val apiRoute: api.v1.Routes = {
    val prefix = "/api/v1alpha"
    wire[api.v1.Routes]
  }
  lazy val termsRoute: terms.Routes = {
    val prefix = "/terms"
    wire[terms.Routes]
  }

}

class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context) = context.environment.mode match {
    case Mode.Dev =>
      new WebGateway(context) with LagomDevModeComponents {}.application
    case _ =>
      new WebGateway(context) {
        override def serviceLocator = NoServiceLocator
      }.application
  }
}

trait ServiceWiring {

}