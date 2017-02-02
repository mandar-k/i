package com.livelygig.product.content.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.content.api.Content
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait ContentCommand

case class AddContent(message: Content) extends ContentCommand with ReplyType[Done]
object AddContent {
  implicit val format: Format[AddContent] = Json.format
}