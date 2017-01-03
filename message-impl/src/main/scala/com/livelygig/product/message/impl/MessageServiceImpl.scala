package com.livelygig.product.message.impl

import java.util.UUID

import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.message.api.MessageService
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

class MessageServiceImpl(registry: PersistentEntityRegistry,
                         cassandraSession: CassandraSession,
                         msgPubSub: MessagePubSub,
                         msgRepo: MessageRepository)
                        (implicit ec: ExecutionContext, mat: Materializer) extends MessageService {

  override def addMessage() = ServiceCall { msg =>
    val msgUid = UUID.randomUUID()
    refFor(msgUid.toString).ask(AddMessage(msg.copy(id = msgUid))).map { _ => null }
  }

  override def getLiveMessages() = ServiceCall {
    live => Future(msgPubSub.refFor(live.userIds(0)).subscriber)
    }

  private def refFor(messageId: String) = registry.refFor[MessageTimelineEntity](messageId)
}
