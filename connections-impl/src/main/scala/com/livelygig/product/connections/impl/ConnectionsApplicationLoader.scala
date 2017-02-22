package com.livelygig.product.connections.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.connections.api.ConnectionsService
import com.livelygig.product.keeper.api.KeeperService
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

/**
 * Created by shubham.k on 09-01-2017.
 */

abstract class ConnectionsApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents
    with LagomKafkaClientComponents {
  override lazy val lagomServer = LagomServer.forServices(
    bindService[ConnectionsService].to(wire[ConnectionsServiceImpl])
  )
  lazy val keeperService = serviceClient.implement[KeeperService]
  wire[KeeperServiceSubscriberForConnections]
  override lazy val jsonSerializerRegistry = ConnectionsJsonSerializerRegistry
}

class ConnectionsApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new ConnectionsApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new ConnectionsApplication(context) with LagomDevModeComponents

}
