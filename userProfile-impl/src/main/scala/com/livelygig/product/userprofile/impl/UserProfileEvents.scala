package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.livelygig.product.userprofile.api.models.UserProfile
import play.api.libs.json.{Format, Json}
import com.livelygig.product.utils.JsonFormats._
/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait UserProfileEvent extends AggregateEvent[UserProfileEvent]   {
  override def aggregateTag: AggregateEventTagger[UserProfileEvent] = UserProfileEvent.Tag
}

object UserProfileEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserProfileEvent](NumShards)
}

case class UserProfileCreated(userProfile:UserProfile) extends UserProfileEvent

object UserProfileCreated {
  implicit val format: Format[UserProfileCreated] = Json.format
}

case object UserProfileActivated extends UserProfileEvent {
  implicit val format: Format[UserProfileActivated.type] = singletonFormat(UserProfileActivated)
}