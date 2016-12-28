package com.livelygig.product.message.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

/**
  * Created by shubham.k on 27-12-2016.
  */
private[impl] class MessageRepository (session: CassandraSession) {
    def getMessages(userId: String) = {
      session.select("SELECT * FROM messages where userId = ?", userId)
    }
}

