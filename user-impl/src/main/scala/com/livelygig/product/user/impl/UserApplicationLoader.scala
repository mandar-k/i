package com.livelygig.product.user.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
//import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.user.api.UserService
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

abstract class UserApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with AhcWSComponents
  with CassandraPersistenceComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[UserService].to(wire[UserServiceImpl])
  )
  lazy val userRepository =wire[UserRepository]
  persistentEntityRegistry.register(wire[UserEntity])
  readSide.register(wire[UserEventProcessor])
}

class UserApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new UserApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new UserApplication(context) with LagomDevModeComponents
}
