package com.livelygig.product.message.impl

import java.util.UUID

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.livelygig.product.keeper.api.models.{UserPermission, UserRole}
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, RequestHeader}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.message.api.MessageService
import com.livelygig.product.security.resource.ResourceServerSecurity
import play.api.mvc.Request

import scala.concurrent.{ExecutionContext, Future}

class MessageServiceImpl(registry: PersistentEntityRegistry,
                         cassandraSession: CassandraSession,
                         msgPubSub: MessagePubSub,
                         msgRepo: MessageRepository,
                         handler: MessageAuthHandler,
                         analyser: ConstraintAnalyser)
                        (implicit ec: ExecutionContext, mat: Materializer) extends MessageService {

  override def addMessage() = /*ResourceServerSecurity.authenticated(())*/
    ResourceServerSecurity.authenticated((authKey, rh) => ServerServiceCall { msg =>
      analyser.hasRolesAndPermissions(List(Array(UserRole("user"))), UserPermission("add"), handler, rh)
          .flatMap{ authFlag => authFlag match {
            case true =>
              val msgUid = UUID.randomUUID()
              refFor(msgUid.toString).ask(AddMessage(msg.copy(id = msgUid))).map { _ => null }
            case false => throw Forbidden("Authorization failed")
          }
        }
    })

 /* override def getLiveMessages() = ResourceServerSecurity.authenticated(userId => ServerServiceCall {
    live => Future(msgPubSub.refFor(live.userIds(0)).subscriber)
    })*/
 override def getLiveMessages() = /*ServerSecurity.authenticated( userId => ServerServiceCall {*/ServiceCall{
   live => Future(msgPubSub.refFor(live.userIds(0)).subscriber)
 }
  //)


  private def refFor(messageId: String) = registry.refFor[MessageTimelineEntity](messageId)
}
