package com.livelygig.product.user.impl


import akka.Done
import com.datastax.driver.core.{PreparedStatement, Row}
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.{ReadSide, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.livelygig.product.user.api
import com.livelygig.product.user.api.User

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 21-12-2016.
  */
private[impl] class UserRepository(session: CassandraSession)(implicit ec: ExecutionContext) {
  def getUser(user: api.User) = {
    session.selectOne("SELECT * from users WHERE email = ? AND password = ?", user.email, user.password)
      .map {
        case Some(userRow) => getUserFromRow(userRow)
        case None =>
          throw NotFound(s"User with email ${user.email}")
      }
  }

  def doesUserExists(user: api.User) = {
    session.selectOne("SELECT * from users WHERE email = ? ", user.email).map {
      case Some(user) => true
      case None => false
    }

  }

  def getUserFromRow(row: Row) = {
    api.User(row.getUUID("id"), row.getString("email"), "", row.getString("name"))
  }
}