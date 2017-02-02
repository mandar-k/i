package com.livelygig.product.alias.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.alias.api.AliasService
import com.livelygig.product.keeper.api.KeeperService
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by shubham.k on 09-01-2017.
  */

abstract class AliasApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents
    with LagomKafkaClientComponents {
  override lazy val lagomServer = LagomServer.forServices(
    bindService[AliasService].to(wire[AliasServiceImpl])
  )
  lazy val keeperService = serviceClient.implement[KeeperService]
  wire[KeeperServiceSubscriberForAlias]
  override lazy val jsonSerializerRegistry = AliasJsonSerializerRegistry
}

class AliasApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new AliasApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new AliasApplication(context) with LagomDevModeComponents

}
