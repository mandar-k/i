package com.livelygig.product.connections.impl

import com.livelygig.product.connections.api.models.Connection
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 14-02-2017.
  */
case class ConnectionState (connectionsAliasUri: Seq[Connection])

object ConnectionState {
  implicit  val format: Format[ConnectionState] = Json.format
}
