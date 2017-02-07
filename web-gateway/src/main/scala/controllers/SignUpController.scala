package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import forms.SignUpForm
import models.services.SilhouetteIdentityService
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Controller
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models._
import utils.auth.DefaultEnv

import scala.concurrent.Future


/**
 * The `Sign Up` controller.
 *
 * @param messagesApi            The Play messages API.
 * @param silhouette             The Silhouette stack.
 * @param userService            The user service implementation.
 * @param authInfoRepository     The auth info repository implementation.
 * @param avatarService          The avatar service implementation.
 * @param webJarAssets           The webjar assets implementation.
 */
class SignUpController @Inject() (
                                   val messagesApi: MessagesApi,
                                   silhouette: Silhouette[DefaultEnv],
                                   userService: SilhouetteIdentityService,
                                   authInfoRepository: AuthInfoRepository,
                                   avatarService: AvatarService,
                                   keeperService: KeeperService,
                                   implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  /**
    * Views the `Sign Up` page.
    *
    * @return The result to display.
    */
  def view = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.signUp(SignUpForm.form)))
  }

  /**
    * Handles the submitted form.
    *
    * @return The result to display.
    */
  def submit = silhouette.UnsecuredAction.async{ implicit request =>
    import com.livelygig.product.keeper.api.models.User
    SignUpForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signUp(form))),
      data => {
        val result = Redirect(routes.SignUpController.view()).flashing("info" -> Messages("sign.up.email.sent", data.email))
//        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
        val userName = data.email.split("@"){0}
        val userAuth = new UserAuth(userName, data.email, data.password)
        val userProfile = new UserProfile("name","profilePic")
        val user = new User(userAuth, userProfile)
        keeperService.createUser.invoke(user).map {
          userAuthRes =>
            userAuthRes match{
              case UserAuthRes(_,ErrorResponse(msg)) =>
                Redirect(routes.SignUpController.view()).flashing("error" -> Messages(msg))
              case UserAuthRes("createUserWaiting", CreateUserResponse("")) =>
//                silhouette.env.eventBus.publish(SignUpEvent(user1, request))
                result
            }
        }
      }
    )
  }
}

