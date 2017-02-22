package com.livelygig.product.connections.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.livelygig.product.connections.api.models.{ConnectionResponse, GetConnectionsRequest, NewConnectionRequest}

/**
 * Created by shubham.k on 02-02-2017.
 */
trait ConnectionsService extends Service {
  def addConnections(): ServiceCall[NewConnectionRequest, ConnectionResponse]
  def getConnections(): ServiceCall[GetConnectionsRequest, ConnectionResponse]

  def descriptor = {
    import Service._
    named("connections").withCalls(
      namedCall("/api/connections/add", addConnections _),
      namedCall("/api/connections/get", getConnections _)
    )
  }
}
