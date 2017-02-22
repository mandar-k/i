package com.livelygig.product.content.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.content.api.ContentService
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._
import com.livelygig.product.keeper.api.KeeperService

abstract class ContentApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents /*with PubSubComponents*/ {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[ContentService].to(wire[ContentServiceImpl])
  )
  override lazy val jsonSerializerRegistry = MessageJsonSerializerRegistry
  lazy val actSys = actorSystem
  //  pubSubRegistry.refFor()
  //  lazy val messageRepository = wire[MessageRepository]
  lazy val msgPubSub = wire[ContentPubSub]
  lazy val msgRepo = wire[ContentRepository]
  //  lazy val msgPubSubRegistry = wire[pubSubRegistry]
  lazy val handler = wire[ContentAuthHandler]
  lazy val analyser = wire[ConstraintAnalyser]
  lazy val keeperService = serviceClient.implement[KeeperService]
  //  lazy val msgPubSubRegistry = wire[pubSubRegistry]
  persistentEntityRegistry.register(wire[ContentEntity])
  readSide.register(wire[ContentEventProcessor])
}

class ContentApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new ContentApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new ContentApplication(context) with LagomDevModeComponents
}