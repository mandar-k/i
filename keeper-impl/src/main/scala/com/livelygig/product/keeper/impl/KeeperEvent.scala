package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.keeper.api.models.{User, UserAuth}
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 11-01-2017.
  */
class KeeperEvent extends AggregateEvent[KeeperEvent] with Jsonable{
  override def aggregateTag :AggregateEventTagger[KeeperEvent] = KeeperEvent.Tag
}

object KeeperEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[KeeperEvent](NumShards)
}

case class UserCreated(user:User) extends KeeperEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

case class UserLogin (user: User) extends KeeperEvent

object UserLogin {
  implicit val format: Format[UserLogin] = Json.format
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