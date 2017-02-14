package com.livelygig.product.connections.api.models

import play.api.libs.json.{Format, Json}
import com.livelygig.product.utils.JsonFormats.enumFormat

/**
  * Created by shubham.k on 13-02-2017.
  */
case class Connection(targetAliasUri: String, status: ConnectionStatus.Status) {
  def changeStatus (newStatus: ConnectionStatus.Status) = copy(status = newStatus)
}

object Connection {
  implicit val format: Format[Connection] = Json.format
}

object ConnectionStatus extends Enumeration {
  type Status = Value
  val Connected, PendingAcceptanceFromMe,PendingAcceptanceFromOther, BlockedByMe, BlockedByOther = Value
  implicit val format:Format[Status] = enumFormat(ConnectionStatus)
}