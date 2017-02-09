package controllers

import java.util.{Date, UUID}

import com.livelygig.product.content.api.ContentService
import com.livelygig.product.content.api.models.UserContent
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.security.resource.ResourceClientSecurity
import com.mohiva.play.silhouette.api.Silhouette
import play.api.Environment
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 29-12-2016.
  */
class MessageController(messageService: ContentService,
                        emailService: EmailNotificationsService,
                        silhouette: Silhouette[DefaultEnv]
                       )(implicit env: Environment, ec: ExecutionContext) extends Controller {
  def addMessage = silhouette.SecuredAction.async(parse.json) { request =>
    request.body.validate[UserContent].map { model =>
      messageService
        .addMessage()
        .handleRequestHeader(ResourceClientSecurity.authenticate(request.identity.userUri))
        .invoke(model).map {
        _ => Ok("Content posted successfully.")
      }
    }.recoverTotal { e =>
      Future {
        BadRequest("Detected error: " + JsError.toJson(e))
      }
    }
  }

  def getAllMessage = silhouette.SecuredAction.async { implicit request =>
    messageService
      .getAllMessages()
      .handleRequestHeader(ResourceClientSecurity.authenticate(request.identity.userUri))
      .invoke()
      .map {messages =>
        Ok(Json.toJson(messages))
      }
  }

  def liveMsg = ???
}
