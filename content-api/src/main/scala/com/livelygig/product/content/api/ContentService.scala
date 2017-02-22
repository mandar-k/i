package com.livelygig.product.content.api

import akka.NotUsed
import akka.stream.scaladsl.Source
//import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait ContentService extends Service {

  def addMessage(): ServiceCall[models.UserContent, NotUsed]
  def getLiveMessages(): ServiceCall[LiveContentRequest, Source[models.UserContent, NotUsed]]
  // TODO make it user specific
  def getAllMessages(): ServiceCall[NotUsed, Seq[models.UserContent]]
  def descriptor = {
    import Service._
    named("messages").withCalls(
      namedCall("/api/messages/add", addMessage _),
      namedCall("/api/messages/live", getLiveMessages _),
      namedCall("/api/messages/getAll", getAllMessages)
    ).withAutoAcl(true) // remove auto access control list
  }
}

