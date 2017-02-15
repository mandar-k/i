package com.livelygig.product.connections.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.connections.api.models.ConnectionResponse
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 13-02-2017.
  */
trait ConnectionCommand


case class AddConnection(connectionAliasUri: String) extends ConnectionCommand with ReplyType[Done]

object AddConnection {
  implicit val format: Format[AddConnection] = Json.format
}

case class DeleteConnection(connectionAliasUri: String) extends ConnectionCommand with ReplyType[ConnectionResponse]

object DeleteConnection {
  implicit val format: Format[AddConnection] = Json.format
}

case object GetConnections extends ConnectionCommand with ReplyType[ConnectionResponse]{
  implicit val format: Format[GetConnections.type ] = singletonFormat(GetConnections)
}

