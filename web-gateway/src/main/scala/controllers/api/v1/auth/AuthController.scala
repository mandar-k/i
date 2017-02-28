package controllers.api.v1.auth

/**
 * Created by shubham.k on 27-02-2017.
 */
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{ActivateUserResponse, ErrorResponse, UserAuthRes}
import com.livelygig.product.shared.InitializeSession
import com.mohiva.play.silhouette.api.{LoginEvent, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.{Clock, Credentials}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.api.v1.AbstractController
import play.api.Configuration
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.Action
import silhouetteservices.SilhouetteIdentityService
import utils.auth.DefaultEnv
import net.ceedubs.ficus.Ficus._
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

/**
 * Created by shubham.k on 27-02-2017.
 */
class AuthController(
    credentialsProvider: CredentialsProvider,
    silhouetteIdentityService: SilhouetteIdentityService,
    silhouette: Silhouette[DefaultEnv],
    configuration: Configuration,
    clock: Clock, keeperService: KeeperService
)(implicit val ec: ExecutionContext) extends AbstractController() {
  def signin = Action.async(parse.json) { implicit request =>
    Future.successful(Ok(""))
    /*unmarshalJsValue[InitializeSession](request) { data =>
      credentialsProvider.authenticate(Credentials(data.email, data.password)).flatMap { loginInfo =>
        silhouetteIdentityService.retrieve(loginInfo).flatMap {
          case Some(user) => for {
            authenticator <- silhouette.env.authenticatorService.create(loginInfo).map(authenticatorWithRememberMe(_, data.rememberMe))
            cookie <- silhouette.env.authenticatorService.init(authenticator)
            result <- silhouette.env.authenticatorService.embed(cookie, Redirect(controllers.routes.ViewController.app))
          } yield {
            silhouette.env.eventBus.publish(LoginEvent(user, request))
            result
          }
          case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
        }
      }.recover {
        case e: ProviderException =>
          Unauthorized(Json.obj("message" -> Messages("invalid.credentials")))
      }
    }.recoverTotal {
      case error =>
        Future.successful(Unauthorized(Json.obj("message" -> Messages("invalid.credentials"))))
    }*/
  }

  private def authenticatorWithRememberMe(authenticator: CookieAuthenticator, rememberMe: Boolean) = {
    val duration = clock.now + rememberMeParams._1.toString()
    if (rememberMe) {
      authenticator.copy(
        expirationDateTime = DateTime.parse(duration),
        idleTimeout = rememberMeParams._2,
        cookieMaxAge = rememberMeParams._3
      )
    } else {
      authenticator
    }
  }

  private lazy val rememberMeParams: (FiniteDuration, Option[FiniteDuration], Option[FiniteDuration]) = {
    val cfg = configuration.getConfig("silhouette.authenticator.rememberMe").get.underlying
    (
      cfg.as[FiniteDuration]("authenticatorExpiry"),
      cfg.getAs[FiniteDuration]("authenticatorIdleTimeout"),
      cfg.getAs[FiniteDuration]("cookieMaxAge")
    )
  }

  /**
   * Activates an account.
   *
   * @param token The token to identify a user.
   * @return The result to display.
   */
  def activate(token: String) = silhouette.UnsecuredAction.async { implicit request =>
    keeperService.activateAccount().invoke(token).map {
      userAuthResponse =>
        userAuthResponse match {
          case UserAuthRes(_, ErrorResponse(msg)) => Redirect(controllers.routes.ViewController.signin()).flashing("error" -> msg)
          case UserAuthRes(_, ActivateUserResponse(msg)) => Redirect(controllers.routes.ViewController.signin()).flashing("success" -> msg)
        }
    }
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signout = silhouette.SecuredAction.async { implicit request =>
    val result = Redirect(controllers.routes.ViewController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
