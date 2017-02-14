package com.livelygig.product.userprofile.impl

import com.livelygig.product.userprofile.api.models.{UserAlias, UserProfile}
import play.api.libs.json.{Format, Json}
import com.livelygig.product.utils.JsonFormats._
/**
  * Created by shubham.k on 10-01-2017.
  */
case class UserProfileState(state: Option[UserProfile], userStatus:UserProfileStatus.Status)  {
  def withStatus (status: UserProfileStatus.Status) = copy(userStatus = status)
  def changeStatus (newStatus: UserProfileStatus.Status) = copy(userStatus = newStatus)
  def addAlias(userAlias: UserAlias): UserProfileState = state match {
    case None => throw new IllegalStateException("User Not found. Alias can't be added.")
    case Some(user) =>
      val newAliases = user.aliases :+ userAlias
      UserProfileState(Some(user.copy(aliases = newAliases)), userStatus)
  }
}

object UserProfileState {
  implicit val format: Format[UserProfileState] = Json.format
  val initialState = UserProfileState(None, UserProfileStatus.DoesNotExist)

}

object UserProfileStatus extends Enumeration {
  type Status = Value
  val Activated, NotActivated, Deleted, Disabled, DoesNotExist = Value
  implicit val format:Format[Status] = enumFormat(UserProfileStatus)
}