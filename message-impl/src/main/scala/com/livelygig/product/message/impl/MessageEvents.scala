package com.livelygig.product.message.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.message.api.Message
import play.api.libs.json.Json

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait MessageEvent extends AggregateEvent[MessageEvent] with Jsonable  {
  override def aggregateTag: AggregateEventTagger[MessageEvent] = MessageEvent.Tag
}

object MessageEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[MessageEvent](NumShards)
}

case class MessagePosted(message: Message) extends MessageEvent

object MessagePosted {
  implicit val format = Json.format[MessagePosted]
}