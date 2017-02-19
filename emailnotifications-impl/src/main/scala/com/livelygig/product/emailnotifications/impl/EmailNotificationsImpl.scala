package com.livelygig.product.emailnotifications.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.mailer.{Email, MailerClient}
import play.api.http.
import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by shubham.k on 25-01-2017.
  */
class EmailNotificationsImpl( val messagesApi: MessagesApi, mailerClient: MailerClient)(implicit ec: ExecutionContext) extends  EmailNotificationsService with I18nSupport{
  override def sendEmail = ServiceCall{ s: String => Future{
    val email = Email(
      "Reset Password",
      "Livelygig@mailinator.com",
      Seq("nirvanictest@mailinator.com"),
      bodyText = Some("A text message"),
      bodyHtml = Some(views.html.resetPassword("@livelygig.com").body )
    )
    mailerClient.send(email)
  }
  }
}
