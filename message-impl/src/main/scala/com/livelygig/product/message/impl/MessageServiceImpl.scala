package com.livelygig.product.message.impl

import akka.stream.Materializer
import com.google.inject.Inject
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Forbidden
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.message.api.MessageService
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.pubsub.PubSubRegistry

import scala.concurrent.{ExecutionContext, Future}

class MessageServiceImpl(registry: PersistentEntityRegistry, cassandraSession: CassandraSession /*, pubSubRegistry: PubSubRegistry*/)
                        (implicit ec: ExecutionContext, mat: Materializer) extends MessageService {

  override def addMessage(userId: String) = ServiceCall { msg =>
    if (userId == msg.userId) {
      refFor(msg.id.toString).ask(AddMessage(msg)).map { _ => null }
    } else {
      throw Forbidden("User not authorized to post this message")
    }

  }

  override def getLiveMessages() = ???

  //  override def def getHistoricalMessages (): ServiceCall[HistoricalMessagesRequest, Source[Message, _]]

  private def refFor(messageId: String) = registry.refFor[MessageTimelineEntity](messageId)
}
