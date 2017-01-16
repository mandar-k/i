package com.livelygig.product.keeper.api.models

import java.util.UUID

import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 11-01-2017.
  */

case class User(id: UUID, userAuth: UserAuth, userProfile: UserProfile)

object User {
  implicit  val format:Format[User] = Json.format
}
case class UserAuth(username: String,email: String, password: String)

object UserAuth {
  implicit val format: Format[UserAuth] = Json.format
}

case class UserWithAuth(userId: UUID)
case class UserProfile(name: String, profilePic: String)

object UserProfile {
  implicit val format: Format[UserProfile] = Json.format
}

case class UserRole(id: UUID, name: String)

object UserRole {
  implicit val format: Format[UserRole] = Json.format
}

case class UserPermission(id: UUID, name: String)

object UserPermission {
  implicit val format: Format[UserPermission] = Json.format
}

case class AuthorizationInfo (roles: Seq[UserRole], permissions: Seq[UserPermission])

object AuthorizationInfo {
  implicit val format: Format[AuthorizationInfo] = Json.format
}

