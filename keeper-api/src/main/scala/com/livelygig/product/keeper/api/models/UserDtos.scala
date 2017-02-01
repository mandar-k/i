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

sealed trait Content {}



case class ErrorResponse (reason: String ) extends Content

object ErrorResponse {
  implicit val format: Format[ErrorResponse] = Json.format
}

case  class InitializeSessionResponse(authToken: String) extends Content

object InitializeSessionResponse {
  implicit val format: Format[InitializeSessionResponse] = Json.format
}

case class CreateUserResponse(msg: String) extends Content

object CreateUserResponse {
  implicit val format:Format[CreateUserResponse] = Json.format
}

object UserAuthRes {
  implicit val format: Format[UserAuthRes] = Json.format
}

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
  /*implicit val contentReads = {
    val err = Json.reads[ErrorResponse]
    val intit = Json.reads[InitializeSessionResponse]
    val creatUser = Json.reads[CreateUserResponse]
    __.read[ErrorResponse](err).map(x => x: Content) | __.read[InitializeSessionResponse](intit).map(x => x: Content) | __.read[CreateUserResponse](creatUser).map(x => x: Content)
  }
  implicit val contentWrites = Writes[Content] {
    case err: ErrorResponse => Json.writes[ErrorResponse].writes(err)
    case init: InitializeSessionResponse =>  Json.writes[InitializeSessionResponse].writes(init)
  }*/
}