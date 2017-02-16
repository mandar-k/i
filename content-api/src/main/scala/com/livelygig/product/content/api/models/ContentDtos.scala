package com.livelygig.product.content.api.models

import julienrf.json.derived
import play.api.libs.json._

/**
  * Created by shubham.k on 30-01-2017.
  */

case class UserAuthRes(msgType: String, content: Content)

object UserAuthRes {
  implicit val format: Format[UserAuthRes] = Json.format
}

sealed trait Content{}

case class UserContent(content: String)

object UserContent {
  implicit val format: Format[UserContent] = Json.format
}
case class ErrorResponse (reason: String ) extends Content

object ErrorResponse {
  implicit val format: Format[ErrorResponse] = Json.format
}

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}