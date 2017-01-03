package com.livelygig.product.message.api

import akka.NotUsed
import akka.stream.scaladsl.Source
//import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait MessageService extends Service {

  def addMessage(): ServiceCall[Message, NotUsed]
  def getLiveMessages(): ServiceCall[LiveMessagesRequest, Source[Message, _]]
//  def getHistoricalMessages(): ServiceCall[HistoricalMessagesRequest, Source[Message, _]]

//  def bidEvents: Topic[]
  def descriptor = {
    import Service._
    named("messages").withCalls(
      namedCall("/api/messages/add", addMessage _),
      namedCall("/api/messages/live", getLiveMessages _)/*,
      namedCall("/api/messages/history", getHistoricalMessages)*/
    )
  }
}


