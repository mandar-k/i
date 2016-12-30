package com.livelygig.product.message.impl

import java.util.Date

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.{ReadSide, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.livelygig.product.message.api.Message

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 28-12-2016.
  */
private[impl] class MessageEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[MessageEvent] {
  private var insertMessageStatement: PreparedStatement = null

  override def buildHandler() = {
    readSide.builder[MessageEvent]("MessageEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => preparedStatements())
      .setEventHandler[MessagePosted](e => insertMessage(e.event.message))
      .build
  }

  override def aggregateTags = MessageEvent.Tag.allTags

  def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS messages (
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
      insertMessageStmt <- session.prepare("INSERT INTO messages(userId,id,content,posttime) VALUES (?, ?, ? ,?)")
    } yield {
      insertMessageStatement = insertMessageStmt
      Done
    }
  }

  def insertMessage(message: Message) = {
    Future.successful(List(insertMessageStatement.bind(message.userId, message.id, message.content, Date.from(message.posttime))))
  }
}
