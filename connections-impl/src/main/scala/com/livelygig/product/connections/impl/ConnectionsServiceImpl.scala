package com.livelygig.product.connections.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.connections.api.ConnectionsService
import com.livelygig.product.connections.api.models.{ConnectionResponse, IntroductionRequestSent}
import com.livelygig.product.security.resource.ResourceServerSecurity

import scala.concurrent.ExecutionContext
/**
  * Created by shubham.k on 25-01-2017.
  */
class ConnectionsServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends ConnectionsService {
  override def addConnections() = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { newConnectionReq =>
    for {
      _ <- connectionEntityAdd(newConnectionReq.aliasUriConnectionA, newConnectionReq.aliasUriConnectionB)
      _ <- connectionEntityAdd(newConnectionReq.aliasUriConnectionB, newConnectionReq.aliasUriConnectionA)
    } yield {
      ConnectionResponse("introductionSuccess", IntroductionRequestSent("Users connected."))
    }
  })

  override def getConnections() = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { getConnectionsRequest =>
    val aliasUri = userUri + s"/${getConnectionsRequest.aliasName}"
    registry.refFor[ConnectionsEntity](aliasUri).ask(GetConnections)

  })
  def connectionEntityAdd(connectionSrc: String, connectionTarget: String) = registry.refFor[ConnectionsEntity](connectionSrc).ask(AddConnection(connectionTarget))
}
