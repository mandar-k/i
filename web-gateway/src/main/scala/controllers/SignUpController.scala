package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordHasherRegistry}
import com.mohiva.play.silhouette.impl.providers._
import forms.SignUpForm
import java.io.File

import models.User
import models.services.{AuthTokenService, UserService}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.mailer.{AttachmentData, AttachmentFile, Email, MailerClient}
import play.api.mvc.Controller
import play.api.libs.mailer._
import java.io.File

import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models._
import org.apache.commons.mail.EmailAttachment
import utils.auth.DefaultEnv

import scala.concurrent.Future


/**
 * The `Sign Up` controller.
 *
 * @param messagesApi            The Play messages API.
 * @param silhouette             The Silhouette stack.
 * @param userService            The user service implementation.
 * @param authInfoRepository     The auth info repository implementation.
 * @param authTokenService       The auth token service implementation.
 * @param avatarService          The avatar service implementation.
 * @param passwordHasherRegistry The password hasher registry.
 * @param mailerClient           The mailer client.
 * @param webJarAssets           The webjar assets implementation.
 */
class SignUpController @Inject() (
                                   val messagesApi: MessagesApi,
                                   silhouette: Silhouette[DefaultEnv],
                                   userService: UserService,
                                   authInfoRepository: AuthInfoRepository,
                                   authTokenService: AuthTokenService,
                                   avatarService: AvatarService,
                                   //  passwordHasherRegistry: PasswordHasherRegistry,
                                   passwordHasher: PasswordHasher,
                                   mailerClient: MailerClient,
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
            /*val user1: models.User =
              models.User(
                userID = UUID.randomUUID(),
                loginInfo = loginInfo,
                firstName = Some(data.firstName),
                lastName = Some(data.lastName),
                fullName = Some(data.firstName + " " + data.lastName),
                email = Some(data.email),
                avatarURL = None,
                activated = false
              )*/
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

