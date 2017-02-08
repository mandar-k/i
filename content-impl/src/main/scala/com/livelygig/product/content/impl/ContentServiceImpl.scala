package com.livelygig.product.content.impl

import java.util.UUID

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.livelygig.product.keeper.api.models.{UserPermission, UserRole}
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, RequestHeader}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.content.api.ContentService
import com.livelygig.product.security.resource.ResourceServerSecurity
import play.api.mvc.Request

import scala.concurrent.{ExecutionContext, Future}

class ContentServiceImpl(registry: PersistentEntityRegistry,
                         cassandraSession: CassandraSession,
                         msgPubSub: ContentPubSub,
                         msgRepo: ContentRepository,
                         handler: ContentAuthHandler,
                         analyser: ConstraintAnalyser)
                        (implicit ec: ExecutionContext, mat: Materializer) extends ContentService {

  override def addMessage() = ServiceCall {
    content =>
      val msgUid = UUID.randomUUID()
      refFor(msgUid.toString).ask(AddContent(content)).map { _ => null }
  }

  /*ResourceServerSecurity.authenticated((authKey, rh) => ServerServiceCall { msg =>
    analyser.hasRolesAndPermissions(List(Array(UserRole("user"))), UserPermission("add"), handler, rh)
        .flatMap{ auth => auth match {
          case true =>
            val msgUid = UUID.randomUUID()
            refFor(msgUid.toString).ask(AddContent(msg)).map { _ => null }
          case false => throw Forbidden("Authorization failed")
        }
      }
  })*/

  /* override def getLiveMessages() = ResourceServerSecurity.authenticated(userId => ServerServiceCall {
     live => Future(msgPubSub.refFor(live.userIds(0)).subscriber)
     })*/
  override def getLiveMessages() = /*ServerSecurity.authenticated( userId => ServerServiceCall {*/ ServiceCall {
    live => Future(msgPubSub.refFor("MESSAGE").subscriber)
  }

  //)


  private def refFor(messageId: String) = registry.refFor[ContentEntity](messageId)
}
