package com.livelygig.product.connections.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.{ServerSecurity}
import com.livelygig.product.connections.api.ConnectionsService
import com.livelygig.product.connections.api.models.{ConnectionResponse, IntroductionRequestSent}

import scala.concurrent.ExecutionContext
/**
 * Created by shubham.k on 25-01-2017.
 */
class ConnectionsServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends ConnectionsService {
  override def addConnections() = ServerSecurity.authenticated(userUri => ServerServiceCall { newConnectionReq =>
    for {
      _ <- connectionEntityAdd(newConnectionReq.aliasUriConnectionA, newConnectionReq.aliasUriConnectionB)
      _ <- connectionEntityAdd(newConnectionReq.aliasUriConnectionB, newConnectionReq.aliasUriConnectionA)
    } yield {
      ConnectionResponse("introductionSuccess", IntroductionRequestSent("Users connected."))
    }
  })

  override def getConnections() = ServerSecurity.authenticated(userUri => ServerServiceCall { getConnectionsRequest =>
    val aliasUri = userUri + s"/${getConnectionsRequest.aliasName}"
    registry.refFor[ConnectionsEntity](aliasUri).ask(GetConnections)

  })
  def connectionEntityAdd(connectionSrc: String, connectionTarget: String) =
    registry.refFor[ConnectionsEntity](connectionSrc).ask(AddConnection(connectionTarget))
}
