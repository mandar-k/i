package com.livelygig.product.connections.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.livelygig.product.connections.api.models.Connection
import play.api.libs.json.{Format, Json}

/**
 * Created by shubham.k on 14-02-2017.
 */
sealed trait ConnectionEvent extends AggregateEvent[ConnectionEvent] {
  override def aggregateTag: AggregateEventTagger[ConnectionEvent] = ConnectionEvent.Tag
}

object ConnectionEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[ConnectionEvent](NumShards)
}

case class ConnectionAdded(newConnection: Connection) extends ConnectionEvent

object ConnectionAdded {
  implicit val format: Format[ConnectionAdded] = Json.format
}