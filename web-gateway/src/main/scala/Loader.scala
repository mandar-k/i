
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.livelygig.product.connections.api.ConnectionsService
import com.livelygig.product.content.api.ContentService
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.userprofile.api.UserProfileService
import silhouetteservices.SilhouetteIdentityServiceImpl
import com.mohiva.play.silhouette.api.{Silhouette, SilhouetteProvider}
import play.api.i18n.I18nComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import controllers.api.v1.auth._
import controllers.api.v1._
import controllers.terms.TermsController
import scala.collection.immutable
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire._
import controllers.{Assets, ViewController, WebJarAssets}
import modules.SilhouetteModule
import play.api.ApplicationLoader.Context
import play.api._
import play.api.cache.EhCacheComponents
import play.api.http.HttpErrorHandler
import play.api.libs.openid.OpenIDComponents
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient
import play.api.mvc.EssentialFilter
import play.filters.headers.SecurityHeadersComponents
import utils.auth.DefaultEnv
import router.Routes

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
    with I18nComponents
    with WebAppComponents

    with LagomServiceClientComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  override lazy val httpErrorHandler: HttpErrorHandler = wire[WebGatewayErrorHandler]
  lazy val routerOption = None
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }
  implicit val env = context.environment

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

  // silhouette service
  lazy val silhouetteIdentityService = wire[SilhouetteIdentityServiceImpl]
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

trait WebAppComponents extends BuiltInComponents
    with SilhouetteModule
    with I18nComponents
    with OpenIDComponents
    with EhCacheComponents
    with WebAppModule
    with SecurityHeadersComponents {
  lazy val silhouette: Silhouette[DefaultEnv] = wire[SilhouetteProvider[DefaultEnv]]
  lazy val wsClient: WSClient = AhcWSClient()
  override lazy val httpFilters: Seq[EssentialFilter] = {
    Seq(securityHeadersFilter)
  }
}