package com.livelygig.product.message.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait MessageService extends Service {

  def addMessage(userId: String): ServiceCall[Message, NotUsed]
  def getLiveMessages(): ServiceCall[LiveMessagesRequest, Source[Message, _]]
//  def getHistoricalMessages(): ServiceCall[HistoricalMessagesRequest, Source[Message, _]]


  def descriptor = {
    import Service._
    named("messages").withCalls(
      pathCall("/api/messages/live/:userId", addMessage _),
      namedCall("/api/messages/live", getLiveMessages _)/*,
      namedCall("/api/messages/history", getHistoricalMessages)*/
    )
  }
}


