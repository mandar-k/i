package com.livelygig.product.keeper.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.livelygig.product.keeper.api.models.{User, UserAuth}
import com.livelygig.product.keeper.impl.models.UserLoginInfo
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 11-01-2017.
  */
class KeeperEvent extends AggregateEvent[KeeperEvent] {
  override def aggregateTag :AggregateEventTagger[KeeperEvent] = KeeperEvent.Tag
}

object KeeperEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[KeeperEvent](NumShards)
}

case class UserCreated(userId: UUID,user:User) extends KeeperEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

case class UserLogin (userId:UUID, userLoginInfo: UserLoginInfo) extends KeeperEvent

object UserLogin {
  implicit val format: Format[UserLogin] = Json.format
}

case class UserLoginFailed(email:String,reason: String) extends KeeperEvent

object UserLoginFailed {
  implicit val format: Format[UserLoginFailed] = Json.format
}

case class LoginFailed(userLoginInfo: UserLoginInfo) extends KeeperEvent

object LoginFailed {
  implicit val format: Format[LoginFailed] = Json.format
}

case class UserDisabled (userAuth: UserAuth) extends KeeperEvent

object UserDisabled {
  implicit val format: Format[UserDisabled] = Json.format
}

case class UserDeleted (userAuth: UserAuth) extends KeeperEvent

object UserDeleted {
  implicit val format: Format[UserDeleted] = Json.format
}

case class TokenCreated(userAuth: UserAuth) extends KeeperEvent

object TokenCreated{
  implicit val format: Format[TokenCreated] = Json.format
}

case class TokenDeleted(userAuth: UserAuth) extends KeeperEvent

object TokenDeleted {
  implicit val format: Format[TokenDeleted] = Json.format
}