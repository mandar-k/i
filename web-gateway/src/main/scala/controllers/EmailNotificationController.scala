package controllers

import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import play.api.Environment
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContext

/**
  *
  */
class EmailNotificationController(emailNotificationsService: EmailNotificationsService )(implicit env: Environment, ec: ExecutionContext) extends Controller{
 def  sendEmail= Action.async {
   emailNotificationsService.sendEmail.invoke("testing data").map {
     msg => Ok("")
   }
 }
}
