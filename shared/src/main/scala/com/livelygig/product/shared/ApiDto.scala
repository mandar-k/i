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

case class ApiDto(msgType: String,content: Content)

object ApiDto {
  implicit val format: Format[ApiDto] = Json.format
  def apply(content: Content): ApiDto = {
    val nm: String  = content.getClass.getSimpleName
    val tnm: String = Character.toLowerCase(nm.charAt(0)) + nm.substring(1)
    ApiDto(tnm, content)
  }
}

sealed trait Content {}

// request content
case class CreateUser(username: String, email: String, password: String)                                 extends Content
case class InitializeSession(usernameOrEmail: String, password: String)                                  extends Content
case object CloseSession                                                                                 extends Content
case class EstablishConnection(aAliasURI: String, bAliasURI: String)                                     extends Content
case object GetProfile                                                                                   extends Content
case object GetConnections
case class GetConnectionsProfiles(aliasUriList: Seq[String])                                             extends Content
case class InsertContent(cnxnxAliasUri: Seq[String], value:String)


// response content
case class ApiError(reason: String)                                                                      extends Content
case object InitialiseSessionResponse                                                                    extends Content
case class UserProfileResponse(profile: UserProfile)                                                     extends Content
case class ConnectionsResponse(aliasUriList: Seq[String])                                                extends Content
case class ConnectionsProfileResponse (profile: Seq[ConnectionsProfileResponse])                         extends Content

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "contentType").format[String])
}
