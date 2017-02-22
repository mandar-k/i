package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.livelygig.product.keeper.api.KeeperService
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.TokenGenerator
import com.livelygig.product.userprofile.api.UserProfileService
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

abstract class UserProfileApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[UserProfileService].to(wire[UserProfileServiceImpl])
  )
  override lazy val jsonSerializerRegistry = UserProfileJsonSerializerRegistry
  lazy val tokenGenerator = wire[TokenGenerator]
  lazy val userRepository = wire[UserProfileRepository]
  persistentEntityRegistry.register(wire[UserProfileEntity])
  readSide.register(wire[UserProfileEventProcessor])
  lazy val keeperService = serviceClient.implement[KeeperService]
  wire[KeeperSubscriberForUserProfiles]
}

class UserProfileApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new UserProfileApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new UserProfileApplication(context) with LagomDevModeComponents
}
