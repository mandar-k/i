package controllers

import com.mohiva.play.silhouette.api.{LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action}
import utils.auth.DefaultEnv
import scala.concurrent.{ExecutionContext, Future}


/**
  * The basic application controller.
  *
  * @param messagesApi            The Play messages API.
  * @param silhouette             The Silhouette stack.
  * @param socialProviderRegistry The social provider registry.
  * @param webJarAssets           The webjar assets implementation.
  */
class ApplicationController(
                             val messagesApi: MessagesApi,
                             silhouette: Silhouette[DefaultEnv],
                             socialProviderRegistry: SocialProviderRegistry,
                             implicit val webJarAssets: WebJarAssets)
                           (implicit val executionContext: ExecutionContext)
  extends AbstractController() with I18nSupport {

  /**
    * Handles the index action.
    *
    * @return The result to display.
    */
  def index() = Action.async { implicit request =>
    Future.successful(Ok(views.html.home()))
  }

  def app(x_auth_token: Option[String]) = Action.async { implicit request =>
    Future.successful(Ok(views.html.app()))
  }

  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }

  def validateToken = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok("Validated"))
  }


  /**
    * Handles the Sign Out action.
    *
    * @return The result to display.
    */
  def signOut = silhouette.SecuredAction.async { implicit request =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
