package com.livelygig.product.content.api

import akka.NotUsed
import akka.stream.scaladsl.Source
//import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait ContentService extends Service {

  def addMessage(): ServiceCall[Content, NotUsed]
  def getLiveMessages(): ServiceCall[LiveContentRequest, Source[Content, NotUsed]]
  def descriptor = {
    import Service._
    named("messages").withCalls(
      namedCall("/api/messages/add", addMessage _),
      namedCall("/api/messages/live", getLiveMessages _)
    ).withAutoAcl(true)
  }
}


