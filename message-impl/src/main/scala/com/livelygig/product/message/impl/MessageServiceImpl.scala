package com.livelygig.product.message.impl

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.message.api.MessageService
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.livelygig.product.messages.impl.MessageEntity

import scala.concurrent.ExecutionContext

class MessageServiceImpl(registry: PersistentEntityRegistry,cassandraSession: CassandraSession)
                        (implicit ec: ExecutionContext, mat: Materializer) extends MessageService {

  override def getmessages = ???

  private def refFor(messageId:String) = registry.refFor[MessageEntity](messageId)
}
