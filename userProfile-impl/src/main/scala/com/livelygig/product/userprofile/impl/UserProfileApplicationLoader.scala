package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.userprofile.api.UserService
//import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

abstract class UserApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with AhcWSComponents
  with CassandraPersistenceComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[UserService].to(wire[UserServiceImpl])
  )
  override lazy val jsonSerializerRegistry = UserProfileJsonSerializerRegistry

  lazy val userRepository =wire[UserProfileRepository]
  persistentEntityRegistry.register(wire[UserProfileEntity])
  readSide.register(wire[UserProfileEventProcessor])
  lazy val keeperService = serviceClient.implement[KeeperService]
  wire[KeeperSubscriberForUserProfiles]
}

class UserApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new UserApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new UserApplication(context) with LagomDevModeComponents
}
