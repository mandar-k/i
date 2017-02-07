package controllers

import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{ActivateUserResponse, ErrorResponse, UserAuthRes}
import com.mohiva.play.silhouette.api._
import models.services.SilhouetteIdentityService
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Controller
import utils.auth.DefaultEnv

import scala.language.postfixOps


/**
 * The `Activate Account` controller.
 *
 * @param messagesApi      The Play messages API.
 * @param silhouette       The Silhouette stack.
 * @param userService      The user service implementation.
 * @param webJarAssets     The WebJar assets locator.
 */
class ActivateAccountController (
                                  val messagesApi: MessagesApi,
                                  keeperService: KeeperService,
                                  silhouette: Silhouette[DefaultEnv],
                                  userService: SilhouetteIdentityService,
                                  implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {


  /**
   * Activates an account.
   *
   * @param token The token to identify a user.
   * @return The result to display.
   */
  def activate(token: String) = silhouette.UnsecuredAction.async { implicit request =>
    keeperService.activateAccount().invoke(token).map{
      userAuthResponse =>
        userAuthResponse match {
          case UserAuthRes(_, ErrorResponse(msg)) => Redirect(routes.SignInController.view()).flashing("error" -> msg)
          case UserAuthRes(_, ActivateUserResponse(msg)) => Redirect(routes.SignInController.view()).flashing("success" -> msg)
        }
    }
  }
}
