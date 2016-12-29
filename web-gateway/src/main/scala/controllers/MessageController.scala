package controllers

import java.time.Instant
import java.util.UUID

import com.livelygig.product.message.api.{Message, MessageService}
import play.api.Environment
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
  * Created by shubham.k on 29-12-2016.
  */
class MessageController(messageService: MessageService) (implicit env: Environment, ec: ExecutionContext) extends Controller{
  def addMessage = Action.async {
    implicit request =>
      messageService.addMessage("sdsdsd").invoke(Message(UUID.randomUUID(),UUID.randomUUID(),"dsdsdsd", Instant.now())).map{
        _ => Ok("")
      }
  }
}
