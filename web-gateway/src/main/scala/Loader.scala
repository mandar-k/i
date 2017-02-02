import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.keeper.api.KeeperService
//import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.message.api.MessageService
import com.livelygig.product.user.api.UserService
import com.mohiva.play.silhouette.api.{Silhouette, SilhouetteProvider}
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import controllers.ActivateAccountController
import controllers.ChangePasswordController
import controllers.ForgotPasswordController
import controllers.ResetPasswordController
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire._
import controllers.ApplicationController
import controllers.Assets
import controllers.MessageController
import controllers.SignInController
import controllers.SignUpController
import controllers.SocialAuthController
import controllers.WebJarAssets
import models.daos.UserDAOImpl
import models.services.UserServiceImpl
import modules.SilhouetteModule
import play.api.ApplicationLoader.Context
import play.api._
import play.api.cache.EhCacheComponents
import play.api.http.HttpErrorHandler
import play.api.libs.mailer.MailerComponents
import play.api.libs.openid.OpenIDComponents
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.filters.csrf.{CSRFComponents, CSRFConfig, CSRFFilter}
import play.filters.headers.SecurityHeadersComponents
import utils.auth.DefaultEnv


abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  with I18nComponents
  //  with AhcWSComponents
  with WebAppComponents

  with LagomServiceClientComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
 override  lazy val httpErrorHandler:HttpErrorHandler = wire[WebGatewayErrorHandler]
  lazy val routerOption = None
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }
  implicit val env = context.environment

  lazy val userServiceImpl = serviceClient.implement[UserService]
  lazy val messageServiceImpl = serviceClient.implement[MessageService]
  lazy val emailNotificationImpl = serviceClient.implement[EmailNotificationsService]
  lazy val keeperService = serviceClient.implement[KeeperService]
  lazy val signupController: SignUpController = wire[SignUpController]
  lazy val signinController: SignInController = wire[SignInController]
  lazy val messageController = wire[MessageController]
  //  lazy val main = wire[Main]
  //  lazy val assets = wire[Assets]
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
  with MailerComponents
   with CSRFComponents
  with SecurityHeadersComponents
  {
    lazy val silhouette: Silhouette[DefaultEnv] = wire[SilhouetteProvider[DefaultEnv]]
  lazy val userService = new UserServiceImpl(new UserDAOImpl)
  lazy val assets: Assets = wire[Assets]
  lazy val applicationController: ApplicationController = wire[ApplicationController]
  lazy val socialAuthController: SocialAuthController = wire[SocialAuthController]



  lazy val webjarAssets: WebJarAssets = wire[WebJarAssets]
  lazy val forgotPasswordController: ForgotPasswordController = wire[ForgotPasswordController]
  lazy val resetPasswordController: ResetPasswordController = wire[ResetPasswordController]
  lazy val changePasswordController: ChangePasswordController = wire[ChangePasswordController]
  lazy val activateAccountController: ActivateAccountController = wire[ActivateAccountController]
  lazy val wsClient: WSClient = AhcWSClient()
  //  lazy val router: Router = {
  //    wire[Routes] withPrefix "/"
  //  }
  override lazy val httpFilters: Seq[EssentialFilter] = {
    Seq(csrfFilter, securityHeadersFilter)
  }
}