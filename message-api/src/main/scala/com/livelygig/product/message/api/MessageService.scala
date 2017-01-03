package com.livelygig.product.message.api

import akka.NotUsed
import akka.stream.scaladsl.Source
//import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait MessageService extends Service {

  def addMessage(): ServiceCall[Message, NotUsed]
  def getLiveMessages(): ServiceCall[LiveMessagesRequest, Source[Message, _]]
  def descriptor = {
    import Service._
    named("messages").withCalls(
      namedCall("/api/messages/add", addMessage _),
      namedCall("/api/messages/live", getLiveMessages _)
    )
  }
}


