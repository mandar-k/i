package com.livelygig.product.message.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.message.api.MessageService
import com.livelygig.product.messages.impl.MessageEntity
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

abstract class MessageApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with AhcWSComponents
  with CassandraPersistenceComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[MessageService].to(wire[MessageServiceImpl])
  )
//  lazy val userRepository =wire[MessagesRepository]
  persistentEntityRegistry.register(wire[MessageEntity])
//  readSide.register(wire[MessageEventProcessor])
}

class MessageApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new MessageApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new MessageApplication(context) with LagomDevModeComponents
}
