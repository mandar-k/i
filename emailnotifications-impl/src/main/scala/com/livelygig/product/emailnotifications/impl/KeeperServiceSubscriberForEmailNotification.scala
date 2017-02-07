package com.livelygig.product.emailnotifications.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import play.api.libs.mailer.{Email, MailerClient}
import com.livelygig.product.keeper.api

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 02-02-2017.
  */
class KeeperServiceSubscriberForEmailNotification(mailerClient: MailerClient, keeperService: KeeperService)(implicit ec: ExecutionContext) {
  keeperService.keeperTopicProducer.subscribe.atLeastOnce(Flow[KeeperEventsForTopics].mapAsync(1) {
    case uc:api.UserCreated =>
      // TODO get the running location of gateway
      val url = s"http://localhost:9000/account/activate/${uc.authToken}"
      println("--------------------------------------------Activation Link--------------------------------------------------")
      println(s"----------------$url-------------------")
      println("-------------------------------------------------------------------------------------------------------------")

      val email = Email(
        "Activate Your Account",
        "Lively Support <tmail9192@gmail.com>",
        Seq(uc.email),
        bodyHtml = Some(views.html.activateAccount(url).body )
      )
      mailerClient.send(email)
      Future(Done)
    case _ =>
      Future.successful(Done)
  })
}
