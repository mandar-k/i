package com.livelygig.product.content.impl

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 28-12-2016.
  */
private[impl] class ContentEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[ContentEvent] {
  private var insertMessageStatement: PreparedStatement = null

  override def buildHandler() = {
    readSide.builder[ContentEvent]("MessageEventOffset")
//      .setGlobalPrepare(createTables)
//      .setPrepare(_ => preparedStatements())
//      .setEventHandler[ContentPosted](e => insertMessage(e.event.message))
      .build
  }

  override def aggregateTags = ContentEvent.Tag.allTags

  def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS msg (
          userId UUID,
          id UUID,
          content text,
          posttime timestamp,
          PRIMARY KEY ((userId), posttime)
          ) WITH CLUSTERING ORDER BY (posttime DESC)
        """
      )
    } yield {
      Done
    }
  }

  def preparedStatements() = {
    for {
      insertMessageStmt <- session.prepare("INSERT INTO msg(userId,id,content,posttime) VALUES (?, ?, ? ,?)")
    } yield {
      insertMessageStatement = insertMessageStmt
      Done
    }
  }

  /*def insertMessage(message: PostContent) = {
    Future.successful(List(insertMessageStatement.bind(/*message.userId, message.id, */message.content, message.posttime)))
  }*/
}
