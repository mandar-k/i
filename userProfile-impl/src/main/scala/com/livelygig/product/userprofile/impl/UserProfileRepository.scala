package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by shubham.k on 21-12-2016.
 */
private[impl] class UserProfileRepository(session: CassandraSession)(implicit ec: ExecutionContext) {
  /*def getUser(user: api.User) = {
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
  }*/
}