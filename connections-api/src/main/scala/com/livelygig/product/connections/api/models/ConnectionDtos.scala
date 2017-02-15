package com.livelygig.product.connections.api.models

import julienrf.json.derived
import play.api.libs.json.{Format, Json, __}

/**
  * Created by shubham.k on 13-02-2017.
  */
case class ConnectionResponse(msgType: String, content: Content)

case class ErrorResponse (reason: String ) extends Content

object ErrorResponse {
  implicit val format: Format[ErrorResponse] = Json.format
}

case  class UserAliasConnectionsList(connections:Seq[Connection] ) extends Content

object ConnectionResponse {
  implicit val format: Format[ConnectionResponse] = Json.format
}

case class IntroductionRequestSent(msg: String) extends Content

case class NewConnectionResponse (connection: Connection) extends Content

object NewConnectionResponse {
  implicit val format: Format[NewConnectionResponse] = Json.format
}

case class NewConnectionRequest(aliasUriConnectionA: String, aliasUriConnectionB: String)

object NewConnectionRequest {
  implicit val format: Format[NewConnectionRequest] = Json.format
}

case class GetConnectionsRequest(aliasName: String)

object GetConnectionsRequest {
  implicit val format: Format[GetConnectionsRequest] = Json.format
}

sealed trait Content {}

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}