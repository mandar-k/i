package com.livelygig.product.connections.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * Created by shubham.k on 02-02-2017.
  */
trait ConnectionsService extends Service {
  def addConnections(): ServiceCall[String, String]

  def descriptor = {
    import Service._
    named("connections").withCalls(
      namedCall("/api/connections/add", addConnections _)
    )
  }
}
