package com.livelygig.product.message.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.pubsub.{PubSubComponents, PubSubRegistry, TopicId}
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.message.api.{Message, MessageService}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

abstract class MessageApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents
    /*with PubSubComponents*/ {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[MessageService].to(wire[MessageServiceImpl])
  )
//  pubSubRegistry.refFor()
  lazy val messageRepository = wire[MessageRepository]
  lazy val msgPubSub = wire[MessagePubSub]
//  lazy val msgPubSubRegistry = wire[pubSubRegistry]
  persistentEntityRegistry.register(wire[MessageTimelineEntity])
  readSide.register(wire[MessageEventProcessor])
}

class MessageApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new MessageApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new MessageApplication(context) with LagomDevModeComponents
}