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

private[impl] class UserEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[UserEvent] {
  private var insertUserAuthStatement: PreparedStatement = null

  override def buildHandler() = {
    readSide.builder[UserEvent]("UserEventOffset")
//      .setGlobalPrepare(createTables)
//      .setPrepare(_ => preparedStatements())
//      .setEventHandler[UserCreated](e => insertUserAuth(e.event.user))
      .build
  }

  def aggregateTags = UserEvent.Tag.allTags

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
        CREATE TABLE IF NOT EXISTS users (
          userId UUID ,
          email text PRIMARY KEY,
          username text,
          password text
        )
      """)
    } yield {
      Done
    }

  }

  private def preparedStatements() = {
    for {
      insertUserAuth <- session.prepare(
        """
          INSERT INTO users (
          userId,
          email,
          username,
          password
        ) VALUES (?, ?, ?, ?)
        """)
    } yield {
      insertUserAuthStatement = insertUserAuth
      Done
    }
  }

  private def insertUserAuth(user: User) = {
    Future.successful(List(insertUserAuthStatement.bind(user.id, user.email, user.name, user.password)))
  }
}
