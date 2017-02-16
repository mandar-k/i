package controllers

import com.livelygig.product.content.api.ContentService
import com.livelygig.product.content.api.models.UserContent
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.shared.ResourceClientSecurity
import com.mohiva.play.silhouette.api.Silhouette
import play.api.Environment
import play.api.libs.json.{JsError, Json}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 29-12-2016.
  */
class MessageController(messageService: ContentService,
                        emailService: EmailNotificationsService,
                        silhouette: Silhouette[DefaultEnv]
                       )(implicit ec: ExecutionContext) extends AbstractController() {
  def addMessage = silhouette.SecuredAction.async(parse.json) { request =>
    unmarshalJsValue[UserContent](request) { model =>
      messageService
        .addMessage()
        .handleRequestHeader(ResourceClientSecurity.authenticate(request.identity.userUri))
        .invoke(model)
        .map {
          _ => Ok("Content posted successfully.")
        }
    }
  }

  def getAllMessage = silhouette.SecuredAction.async { implicit request =>
    messageService
      .getAllMessages()
      .handleRequestHeader(ResourceClientSecurity.authenticate(request.identity.userUri))
      .invoke()
      .map { messages =>
        Ok(Json.toJson(messages))
      }
  }

  def liveMsg = ???
}
