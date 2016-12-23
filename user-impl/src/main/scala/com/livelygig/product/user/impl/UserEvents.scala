package com.livelygig.product.user.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.user.api.User
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait UserEvent extends AggregateEvent[UserEvent] with Jsonable  {
  override def aggregateTag: AggregateEventTagger[UserEvent] = UserEvent.Tag
}

object UserEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserEvent](NumShards)
}

case class UserCreated(user: User) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}