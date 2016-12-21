package com.livelygig.product.user.impl

import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.livelygig.product.user.api
import com.livelygig.product.user.api.User

import scala.concurrent.ExecutionContext

/**
  * Created by shubham.k on 21-12-2016.
  */
private[impl] class UserRepository(session:CassandraSession)(implicit ec: ExecutionContext) {
  def getUser(user: User) = {
    session.selectOne("SELECT * from user WHERE email = ? AND password = ?", user.email, user.password)
      .map{
        case Some(userRow) => getUserFromRow(userRow)
        case None =>
          throw NotFound(s"User with email ${user.email}")
      }
  }

  def doesUserExists(user: User) = {
    session.selectOne("SELECT * from User WHERE email = ? ", user.email).map{
      case Some(user) => true
      case None => false
    }

  }

  def getUserFromRow(row: Row) = {
    api.User(row.getUUID("id"),row.getString("email"),"",row.getString("name"))
  }
}
