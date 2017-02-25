package com.livelygig.product.content.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.pubsub.{PubSubRegistry, TopicId}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.ServerSecurity
import com.livelygig.product.content.api.ContentService
import com.livelygig.product.content.api.models.UserContent

import scala.concurrent.{ExecutionContext, Future}

class ContentServiceImpl(
    registry: PersistentEntityRegistry,
    pubSubRegistry: PubSubRegistry,
    handler: ContentAuthHandler,
    analyser: ConstraintAnalyser,
    system: ActorSystem
)(implicit ec: ExecutionContext, mat: Materializer) extends ContentService {

  override def addMessage() = ServerSecurity.authenticated(userUri => ServerServiceCall { content =>
    val msgUid = UUID.randomUUID()
    refFor(msgUid.toString).ask(AddContent(content)).map { _ => null }
  })

  override def getLiveMessages() = /*ServerSecurity.authenticated( userId => ServerServiceCall {*/ ServiceCall {
    live => Future(pubSubRegistry.refFor(TopicId[UserContent]("MESSAGE")).subscriber)
  }

  //)
  //TODO make it user specific
  override def getAllMessages() = ServerSecurity.authenticated(userUri => ServerServiceCall { _ =>
    val currentIdsQuery = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
    currentIdsQuery.currentPersistenceIds()
      .filter(_.startsWith("ContentEntity|"))
      .mapAsync(4) { id =>
        val entityId = id.split("\\|", 2).last
        refFor(entityId)
          .ask(GetContent)
          .map(_.map(e => e))
      }.collect {
        case Some(content) => content
      }
      .runWith(Sink.seq)
  })

  private def refFor(messageId: String) = registry.refFor[ContentEntity](messageId)
}
