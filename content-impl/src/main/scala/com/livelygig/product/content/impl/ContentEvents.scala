package com.livelygig.product.content.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.livelygig.product.content.api.models.{UserContent}
import play.api.libs.json.Json

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait ContentEvent extends AggregateEvent[ContentEvent]   {
  override def aggregateTag: AggregateEventTagger[ContentEvent] = ContentEvent.Tag
}

object ContentEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[ContentEvent](NumShards)
}

case class ContentPosted(content: UserContent) extends ContentEvent

object ContentPosted {
  implicit val format = Json.format[ContentPosted]
}