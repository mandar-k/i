package com.livelygig.product.messages.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.message.api.Message
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait MessageCommand extends Jsonable

case class PostMessage(message: Message) extends MessageCommand with ReplyType[Done]
object PostMessage {
  implicit val format: Format[PostMessage] = Json.format
}