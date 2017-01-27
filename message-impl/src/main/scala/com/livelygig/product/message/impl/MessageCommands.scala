package com.livelygig.product.message.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.message.api.Message
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait MessageCommand

case class AddMessage(message: Message) extends MessageCommand with ReplyType[Done]
object AddMessage {
  implicit val format: Format[AddMessage] = Json.format
}