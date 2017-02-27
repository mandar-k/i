package com.livelygig.product.shared

import julienrf.json.derived
import play.api.libs.json.{Format, Json, __}


/**
  * For now the dtos are shared between microserices, web gateway
  * and the scala.js client. Which is Awesome :) However that would mean
  * every microservice would need to have a dependency for this shared package
  * which kind of defeats the purpose of domain driven architecture
  * However till architecture is stable enough we will continue with this shared structure.
  */

sealed trait Content {}

case class Message(body: String)                                                                         extends Content
case class CreateUser(email: String, password: String)                                                   extends Content
case class InitializeSession(email: String, password: String, rememberMe:Boolean)                        extends Content
case class EstablishConnection(aAliasURI: String, bAliasURI: String)                                     extends Content
case class GetConnectionsProfiles(aliasUriList: Seq[String])                                             extends Content
case class InsertContent(cnxnxAliasUri: Seq[String], value:String)                                       extends Content


case class ConnectionsResponse(aliasUriList: Seq[String])                                                extends Content
case class Project(name: String, startDate: String)                                                      extends Content
case class ApiError(reason: String)                                                                      extends Content

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}