package controllers

import com.mohiva.play.silhouette.api.Silhouette
import forms.{SignInForm, SignUpForm}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import utils.auth.DefaultEnv

import scala.concurrent.{Future}

/**
 * Created by shubham.k on 27-02-2017.
 */
class ViewController(
  val messagesApi: MessagesApi,
  silhouette: Silhouette[DefaultEnv],
  implicit val webJarAssets: WebJarAssets
)
    extends Controller with I18nSupport {

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
    Future.successful(Ok(views.html.signIn(SignInForm.form)))
  }

}
