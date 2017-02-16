package com.livelygig.product.shared

import julienrf.json.derived
import play.api.libs.json.{Format, Json, __}

// actual API
case class ApiDto(msgType: String , content: Content)

object ApiDto {
  implicit val format: Format[ApiDto] = Json.format
  def apply(content: Content) = {
    val nm: String  = content.getClass.getSimpleName
    val tnm: String = Character.toLowerCase(nm.charAt(0)) + nm.substring(1)
    ApiDto(tnm, content)
  }
}

sealed trait Content {}

// request content
case class CreateUser(username: String, email: String, password: String)                                 extends Content
case class GetConnectionProfiles(sessionURI: String)                                                     extends Content
case class SessionPing(number: Int)                                                                      extends Content
case class InitializeSession(usernameOrEmail: String, password: String)                                  extends Content
case object CloseSession                                                                                 extends Content
case class EstablishConnection(aAliasURI: String, bAliasURI: String)                                     extends Content
case object GetProfile                                                                                   extends Content
case object GetConnections
case class GetConnectionsProfiles(aliasUriList: Seq[String])                                             extends Content

// response content
case class ApiError(reason: String)                                                                      extends Content
case object InitialiseSessionResponse                                                                    extends Content
case class UserProfileResponse(profile: UserProfile)                                                     extends Content
case class ConnectionsResponse(aliasUriList: Seq[String])                                                extends Content
case class ConnectionsProfileResponse (profile: Seq[ConnectionsProfileResponse])                         extends Content


object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}
