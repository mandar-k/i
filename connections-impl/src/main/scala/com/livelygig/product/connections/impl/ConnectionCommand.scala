package com.livelygig.product.connections.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.connections.api.models.ConnectionResponse
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

