package com.livelygig.product.emailnotifications.impl

import javax.inject.Inject

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.livelygig.product.emailnotifications.api.EmailNotificationsService
import play.api.Environment
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.libs.mailer.{AttachmentFile, _}

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by shubham.k on 25-01-2017.
  */
class EmailNotificationsImpl( val messagesApi: MessagesApi, mailerClient: MailerClient , environment: Environment)(implicit ec: ExecutionContext) extends  EmailNotificationsService with I18nSupport{
  override def sendEmail = ServiceCall{ s: String => Future{
    val email = Email(
      "Reset Password",
      "Livelygig",
      Seq("nirvanictest@mailinator.com"),
      bodyText = Some("A text message"),
      bodyHtml = Some(views.html.resetPassword("@livelygig.com").body )
    )
    mailerClient.send(email)
  }
  }
}
