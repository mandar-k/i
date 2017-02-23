package com.livelygig.product.emailnotifications.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import com.livelygig.product.keeper.api.KeeperService
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._
import play.api.libs.mailer._
import play.api.i18n.{I18nComponents, MessagesApi}
/**
 * Created by shubham.k on 09-01-2017.
 */

abstract class EmailNotificationsApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents
    with MailerComponents
    with I18nComponents
    with LagomKafkaClientComponents {
  override lazy val lagomServer = LagomServer.forServices(
    bindService[EmailNotificationsService].to(wire[EmailNotificationsImpl])
  )
  lazy val keeperService = serviceClient.implement[KeeperService]
  wire[KeeperServiceSubscriberForEmailNotification]
  //  override lazy val jsonSerializerRegistry = EmailNotificationJsonSerializerRegistry

  //  lazy val keeperRepo = wire[KeeperRepository]
  //  persistentEntityRegistry.register(wire[KeeperEntity])
  //  readSide.register(wire[KeeperEventProcessor])
}

class EmailNotificationsApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new EmailNotificationsApplication(context) {
    override def serviceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new EmailNotificationsApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[EmailNotificationsService]
  )

}
