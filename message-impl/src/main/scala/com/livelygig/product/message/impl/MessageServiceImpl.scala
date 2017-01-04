package com.livelygig.product.message.impl

import java.util.UUID

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Forbidden
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.message.api.MessageService
import com.livelygig.product.security.ServerSecurity

import scala.concurrent.{ExecutionContext, Future}

class MessageServiceImpl(registry: PersistentEntityRegistry,
                         cassandraSession: CassandraSession,
                         msgPubSub: MessagePubSub,
                         msgRepo: MessageRepository)
                        (implicit ec: ExecutionContext, mat: Materializer) extends MessageService {

  override def addMessage = ServerSecurity.authenticated( userId => ServerServiceCall { msg =>
    val msgUid = UUID.randomUUID()
    refFor(msgUid.toString).ask(AddMessage(msg.copy(id = msgUid))).map { _ => null }
  })

  override def getLiveMessages() = ServerSecurity.authenticated( userId => ServerServiceCall {
    live => Future(msgPubSub.refFor(live.userIds(0)).subscriber)
    })


  //override def getLiveMessages() = ???/*ServiceCall{ livemsgreq =>
  //  val userId = livemsgreq.userIds

  //}*/

  private def refFor(messageId: String) = registry.refFor[MessageTimelineEntity](messageId)
}
