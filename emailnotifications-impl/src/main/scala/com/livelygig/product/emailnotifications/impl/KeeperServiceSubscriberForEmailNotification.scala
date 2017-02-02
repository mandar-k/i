package com.livelygig.product.emailnotifications.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import play.api.libs.mailer.{Email, MailerClient}
import com.livelygig.product.keeper.api

import scala.concurrent.Future

/**
  * Created by shubham.k on 02-02-2017.
  */
class KeeperServiceSubscriberForEmailNotification(mailerClient: MailerClient, keeperService: KeeperService) {
  keeperService.keeperTopicProducer.subscribe.atLeastOnce(Flow[KeeperEventsForTopics].mapAsync(1) {
    case uc:api.UserCreated =>
      val email = Email(
        "User Authentication Mail",
        "tmail9192@gmail.com",
        Seq(uc.email),
        bodyText = Some("A text message"),
        bodyHtml = Some(views.html.emailNotification("nirvanictest@mailinator.com").body )
      )
      mailerClient.send(email)
      Future.successful(Done)
    case _ =>
      Future.successful(Done)
  })
}
