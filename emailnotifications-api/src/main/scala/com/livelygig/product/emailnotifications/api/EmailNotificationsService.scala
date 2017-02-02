package com.livelygig.product.emailnotifications.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import akka.NotUsed


/**
  * Created by shubham.k on 25-01-2017.
  */
trait EmailNotificationsService extends Service {

  def sendEmail : ServiceCall[String, String]
  def descriptor = {
    import Service._
    named("emailnotifications").withCalls(
      namedCall("/api/emails/send", sendEmail _)
//      namedCall("/api/notifications/send", sendNotifications _)
    ).withAutoAcl(true)
  }
}
