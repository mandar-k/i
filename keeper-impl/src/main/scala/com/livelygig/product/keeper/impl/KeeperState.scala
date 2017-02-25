package com.livelygig.product.keeper.impl

import com.livelygig.product.keeper.api.models.UserAuth
import play.api.libs.json.{Format, Json}
import com.livelygig.product.utils.JsonFormats.enumFormat

/**
 * Created by shubham.k on 10-01-2017.
 */
case class KeeperState(state: Option[UserAuth], userStatus: UserStatus.Status) {
  def withStatus(status: UserStatus.Status) = copy(userStatus = status)
  def changeStatus(newStatus: UserStatus.Status) = copy(userStatus = newStatus)
}

object KeeperState {
  implicit val format: Format[KeeperState] = Json.format
  val initialState = KeeperState(None, UserStatus.DoesNotExist)

}

object UserStatus extends Enumeration {
  type Status = Value
  val Activated, NotActivated, Deleted, Disabled, DoesNotExist = Value
  implicit val format: Format[Status] = enumFormat(UserStatus)
}