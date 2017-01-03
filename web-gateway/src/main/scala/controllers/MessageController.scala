package controllers

import java.time.Instant
import java.util.UUID

import com.livelygig.product.message.api.{LiveMessagesRequest, Message, MessageService}
import play.api.Environment
import play.api.libs.json.JsValue
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
  * Created by shubham.k on 29-12-2016.
  */
class MessageController(messageService: MessageService) (implicit env: Environment, ec: ExecutionContext) extends Controller{
  val userID = UUID.randomUUID
  def addMessage = Action {
    implicit request =>
      messageService.addMessage().invoke(Message(UUID.randomUUID(),userID,"dsdsdsd", Instant.now())).map{
        _ => Ok("")
      }
      Ok("")
  }

  def liveMsg: WebSocket = ???/*WebSocket.acceptOrResult[JsValue, JsValue] {
    implicit  request =>
      messageService.getLiveMessages().invoke(LiveMessagesRequest(Seq(userID.toString))).map {
        e => e.
      }
  }*/
}
