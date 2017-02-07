package com.livelygig.product.keeper.api.models

import julienrf.json.derived
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.util.Try

/**
  * Created by shubham.k on 30-01-2017.
  */

case class UserAuthRes(msgType: String, content: Content)

object UserAuthRes {
  implicit val format: Format[UserAuthRes] = Json.format
}

sealed trait Content {}

case class ErrorResponse (reason: String ) extends Content

object ErrorResponse {
  implicit val format: Format[ErrorResponse] = Json.format
}

case  class InitializeSessionResponse(agentUri: String, email: String, username: String) extends Content

object InitializeSessionResponse {
  implicit val format: Format[InitializeSessionResponse] = Json.format
}

case class CreateUserResponse(msg: String) extends Content

object CreateUserResponse {
  implicit val format:Format[CreateUserResponse] = Json.format
}

case class ActivateUserResponse (msg: String) extends Content

object ActivateUserResponse {
  implicit val format: Format[ActivateUserResponse] = Json.format
}

case class UserFound (userUri: String, username: String, email: String) extends Content

object UserFound {
  implicit val format: Format[UserFound] = Json.format
}
object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}