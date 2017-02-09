package com.livelygig.product.content.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

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

}

