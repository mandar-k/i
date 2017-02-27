package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import forms.{SignInForm, SignUpForm}
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, Controller}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by shubham.k on 27-02-2017.
 */
class ViewController(
  val messagesApi: MessagesApi,
  silhouette: Silhouette[DefaultEnv],
  socialProviderRegistry: SocialProviderRegistry
)
    extends Controller {

  def index = Action.async { implicit request =>
    Future.successful(Ok(views.html.home()))
  }

  def about() = Action.async { implicit request =>
    Future.successful(Ok(views.html.about()))
  }

  def app = Action.async { implicit request =>
    Future.successful(Ok(views.html.app()))
  }

  def signup = Action.async { implicit request =>
    Future.successful(Ok(views.html.signUp(SignUpForm.form)))
  }

  def signin = Action.async { implicit request =>
    Future.successful(Ok(views.html.signIn(SignInForm.form, socialProviderRegistry)))
  }

}
