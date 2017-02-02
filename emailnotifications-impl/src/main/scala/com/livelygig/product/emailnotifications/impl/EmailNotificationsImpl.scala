package com.livelygig.product.emailnotifications.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import play.api.Environment
import play.api.libs.mailer.{AttachmentFile, _}
import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by shubham.k on 25-01-2017.
  */
class EmailNotificationsImpl(mailerClient: MailerClient , environment: Environment)(implicit ec: ExecutionContext) extends  EmailNotificationsService {
  override def sendEmail = ServiceCall{ s: String => Future{
    val cid = "1234"
    val email = Email(
      "User Authentication Mail",
      "tmail9192@gmail.com",
      Seq("nirvanictest@mailinator.com"),
      bodyText = Some("A text message"),
      bodyHtml = Some(views.html.emailNotification("nirvanictest@mailinator.com").body )
    )
    mailerClient.send(email)
  }
  }
}
