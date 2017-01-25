package com.livelygig.product.emailnotifications.impl

import scala.concurrent.ExecutionContext
import com.livelygig.product.emailnotifications.api.{EmailNotificationsService}


/**
  * Created by shubham.k on 25-01-2017.
  */
class EmailNotificationsImpl()(implicit ec: ExecutionContext) extends  EmailNotificationsService {
  override def sendEmail() = ???
}
