package com.livelygig.product.message.impl

import java.time.Instant
import java.util.Date

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.livelygig.product.message.api.Message

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 27-12-2016.
  */
private[impl] class MessageRepository(session: CassandraSession)(implicit ec: ExecutionContext) {
  /*def getMessages(userId: String): Seq[Message] = {
    for {
      rows <- session.selectAll("SELECT * FROM msg where userId = ? ORDER BY timestamp", userId)
      row <- rows
    } yield convertMessage(row)

  }*/

  def convertMessage(item: Row) = {
    Message(
      id = item.getUUID("id"),
      userId = item.getUUID("userId"),
      content = item.getString("content"),
      posttime = item.getTimestamp("timestamp").toInstant
    )
  }
}

