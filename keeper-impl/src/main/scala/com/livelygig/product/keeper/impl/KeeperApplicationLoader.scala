package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.livelygig.product.keeper.api.KeeperService
import com.softwaremill.macwire._


/**
  * Created by shubham.k on 09-01-2017.
  */

abstract class KeeperApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {
  override lazy val lagomServer = LagomServer.forServices(
    bindService[KeeperService].to(wire[KeeperServiceImpl])
  )
  lazy val keeperRepo = wire[KeeperRepository]
  persistentEntityRegistry.register(wire[KeeperEntity])
  readSide.register(wire[KeeperEventProcessor])
}

class KeeperApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new KeeperApplication(context) {
    override def serviceLocator = NoServiceLocator
  }
  override def loadDevMode(context: LagomApplicationContext) =
    new KeeperApplication(context) with LagomDevModeComponents

}
