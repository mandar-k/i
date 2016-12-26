package com.livelygig.product.messages.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.message.api.Message
import com.livelygig.product.message.impl.MessageEvent

/**
  * Created by shubham.k on 16-12-2016.
  */
class MessageEntity extends PersistentEntity{
  override type Command = MessageCommand

  override type Event = MessageEvent

  override type State = Option[Message]

  override def initialState = None

  override def behavior = {
    case None => ???
    case Some(user) => ???
  }
}