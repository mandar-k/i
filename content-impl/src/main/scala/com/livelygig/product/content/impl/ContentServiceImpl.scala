package com.livelygig.product.content.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.ResourceServerSecurity
import com.livelygig.product.content.api.ContentService

import scala.concurrent.{ExecutionContext, Future}

class ContentServiceImpl(
  registry: PersistentEntityRegistry,
    msgPubSub: ContentPubSub,
    handler: ContentAuthHandler,
    analyser: ConstraintAnalyser,
    system: ActorSystem
)(implicit ec: ExecutionContext, mat: Materializer) extends ContentService {

  override def addMessage() = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { content =>
    val msgUid = UUID.randomUUID()
    refFor(msgUid.toString).ask(AddContent(content)).map { _ => null }
  })

  override def getLiveMessages() = /*ServerSecurity.authenticated( userId => ServerServiceCall {*/ ServiceCall {
    live => Future(msgPubSub.refFor("MESSAGE").subscriber)
  }

  //)
  //TODO make it user specific
  override def getAllMessages() = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { _ =>
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
