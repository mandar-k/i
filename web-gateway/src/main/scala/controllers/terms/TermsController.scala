package controllers.terms

import controllers.WebJarAssets
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by shubham.k on 27-02-2017.
 */
class TermsController(val messagesApi: MessagesApi, implicit val webJarAssets: WebJarAssets)(implicit ec: ExecutionContext)
    extends Controller with I18nSupport {

  def copyrights( /*version: Option[String]*/ ) = Action.async { implicit request =>
    Future.successful(Ok(views.html.copyrights()))
  }

  def legal( /*version: Option[String]*/ ) = Action.async { implicit request =>
    Future.successful(Ok(views.html.legal()))
  }

  def terms( /*version: Option[String]*/ ) = Action.async { implicit request =>
    Future.successful(Ok(views.html.terms()))
  }

  def privacy( /*version: Option[String]*/ ) = Action.async { implicit request =>
    Future.successful(Ok(views.html.privacy()))
  }

}
