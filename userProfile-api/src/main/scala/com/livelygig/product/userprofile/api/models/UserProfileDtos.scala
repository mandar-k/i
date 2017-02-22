package com.livelygig.product.userprofile.api.models

import julienrf.json.derived
import play.api.libs.json._

/**
 * Created by shubham.k on 30-01-2017.
 */

case class UserProfileResponse(msgType: String, content: Content)

object UserProfileResponse {
  implicit val format: Format[UserProfileResponse] = Json.format
}

sealed trait Content {}

case class ErrorResponse(reason: String) extends Content

object ErrorResponse {
  implicit val format: Format[ErrorResponse] = Json.format
}

case class UserProfile(name: Option[String], avatar: Option[String], aliases: Seq[UserAlias]) extends Content

object UserProfile {
  implicit val format: Format[UserProfile] = Json.format
}

case class UserConnectionProfile(aliasUri: String, name: Option[String], avatar: Option[String])

object UserConnectionProfile {
  implicit val format: Format[UserConnectionProfile] = Json.format
}
case class ProfileListResponse(profileList: Seq[UserConnectionProfile]) extends Content

object ProfileListResponse {
  implicit val format: Format[ProfileListResponse] = Json.format
}

case class UserAlias(name: String, isDefault: Boolean)

object UserAlias {
  implicit val format: Format[UserAlias] = Json.format
}

object Content {
  implicit val format: Format[Content] =
    derived.flat.oformat((__ \ "type").format[String])
}