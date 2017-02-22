package controllers

import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import play.api.Environment
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContext

/**
 *
 */
class EmailNotificationController(val messagesApi: MessagesApi, emailNotificationsService: EmailNotificationsService)(implicit env: Environment, ec: ExecutionContext) extends Controller with I18nSupport {
  def sendEmail = Action.async {
    emailNotificationsService.sendEmail.invoke("testing data").map {
      msg => Ok("")
    }
  }
}
