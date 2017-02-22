package controllers

import com.mohiva.play.silhouette.api._
import forms.ForgotPasswordForm
import models.services.SilhouetteIdentityService
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Controller
import utils.auth.DefaultEnv

import scala.concurrent.Future

/**
 * The `Forgot Password` controller.
 *
 * @param messagesApi      The Play messages API.
 * @param silhouette       The Silhouette stack.
 * @param userService      The user service implementation.
 * @param webJarAssets     The WebJar assets locator.
 */
class ForgotPasswordController(
  val messagesApi: MessagesApi,
  silhouette: Silhouette[DefaultEnv],
  userService: SilhouetteIdentityService,
  implicit val webJarAssets: WebJarAssets
)
    extends Controller with I18nSupport {

  /**
   * Views the `Forgot Password` page.
   *
   * @return The result to display.
   */
  def view = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.forgotPassword(ForgotPasswordForm.form)))
  }

  /**
   * Sends an email with password reset instructions.
   *
   * It sends an email to the given address if it exists in the database. Otherwise we do not show the user
   * a notice for not existing email addresses to prevent the leak of existing email addresses.
   *
   * @return The result to display.
   */
  def submit = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(""))
    /*ForgotPasswordForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.forgotPassword(form))),
      email => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, email)
        val result = Redirect(routes.SignInController.view()).flashing("info" -> Messages("reset.email.sent"))
        userService.retrieve(loginInfo).flatMap {
          case Some(user) if user.email.isDefined =>
            authTokenService.create(user.userUri).map { authToken =>
              val url = routes.ResetPasswordController.view(authToken.id).absoluteURL()

              mailerClient.send(Email(
                subject = Messages("email.reset.password.subject"),
                from = Messages("email.from"),
                to = Seq(email),
                bodyText = Some(views.txt.emails.resetPassword(user, url).body),
                bodyHtml = Some(views.html.emails.resetPassword(user, url).body)
              ))
              result
            }
          case None => Future.successful(result)
        }
      }
    )*/
  }
}
