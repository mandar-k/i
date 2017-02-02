package com.livelygig.product.content.impl

import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.livelygig.product.content.api.Content

import scala.concurrent.ExecutionContext

/**
  * Created by shubham.k on 27-12-2016.
  */
private[impl] class ContentRepository(session: CassandraSession)(implicit ec: ExecutionContext) {
  /*def getMessages(userId: String): Seq[Message] = {
    for {
      rows <- session.selectAll("SELECT * FROM msg where userId = ? ORDER BY timestamp", userId)
      row <- rows
    } yield convertMessage(row)

  }*/

  def convertMessage(item: Row) = {
    Content(
      id = item.getUUID("id"),
      userId = item.getUUID("userId"),
      content = item.getString("content"),
      posttime = item.getTimestamp("timestamp") //.toInstant
    )
  }
}

