package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

/**
 * The user object.
 *
 * @param userUri   The unique uri of the user.
 * @param loginInfo The linked login info.
 */
case class UserIdentity(userUri: String, loginInfo: LoginInfo, userName: String) extends Identity {
}
