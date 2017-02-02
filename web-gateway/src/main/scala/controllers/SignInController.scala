package controllers

import java.util.UUID
import javax.inject.Inject

import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{ErrorResponse, InitializeSessionResponse, UserAuthRes, UserLoginModel}
import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers._
import forms.SignInForm
import models.services.UserService
import net.ceedubs.ficus.Ficus._
import play.api.Configuration
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Controller
import utils.auth.DefaultEnv

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * The `Sign In` controller.
  *
  * @param messagesApi            The Play messages API.
  * @param silhouette             The Silhouette stack.
  * @param userService            The user service implementation.
  * @param authInfoRepository     The auth info repository implementation.
  * @param credentialsProvider    The credentials provider.
  * @param socialProviderRegistry The social provider registry.
  * @param configuration          The Play configuration.
  * @param clock                  The clock instance.
  * @param webJarAssets           The webjar assets implementation.
  */
class SignInController(
                        val messagesApi: MessagesApi,
                        silhouette: Silhouette[DefaultEnv],
                        userService: UserService,
                        authInfoRepository: AuthInfoRepository,
                        credentialsProvider: CredentialsProvider,
                        socialProviderRegistry: SocialProviderRegistry,
                        configuration: Configuration,
                        clock: Clock,
                        keeperService: KeeperService,
                        implicit val webJarAssets: WebJarAssets)
  extends Controller with I18nSupport {

  /**
    * Views the `Sign In` page.
    *
    * @return The result to display.
    */
  def view = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.signIn(SignInForm.form, socialProviderRegistry)))
  }

  /**
    * Handles the submitted form.
    *
    * @return The result to display.
    */
  def submit = silhouette.UnsecuredAction.async { implicit request =>
    SignInForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signIn(form, socialProviderRegistry))),
      data => {
        val userLoginModel = new UserLoginModel(usernameOrEmail = data.email, data.password)
        keeperService.login.invoke(userLoginModel).map {
          userAuthRes =>
            userAuthRes match {
              case UserAuthRes(_, ErrorResponse(msg)) =>
                Redirect(routes.SignInController.view()).flashing("error" -> Messages(msg))
              case _ =>
                Redirect(routes.ApplicationController.index())
            }}
      }
    )
  }
}
