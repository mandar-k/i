package com.livelygig.product.keeper.impl

import java.util.UUID

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.livelygig.product.keeper.api.models.UserAuth

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 11-01-2017.
  */
private[impl] class KeeperEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[KeeperEvent] {
  private var insertAuthKeyStatement: PreparedStatement = _
  private var deleteAuthKeyStatement: PreparedStatement = _
  private var insertUserPermissions: PreparedStatement = _
  private var insertUserRoles: PreparedStatement = _
  private var updateUserRoles: PreparedStatement = _
  private var updateUserPermissions: PreparedStatement = _

  override def buildHandler() = {
    readSide.builder[KeeperEvent]("keeperEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(tag => preparedStatements)
      .setEventHandler[UserLogin](e => insertToken(e.event.user.id))
      .setEventHandler[UserCreated](e => insertDefaultRolesAndPermissions(e.event.user.id))
//      .setEventHandler[UserDeleted](e => removeUser(e.event.user.id))
//      .setEventHandler[TokenCreated](e => )
      .setEventHandler[TokenDeleted](removeToken)
      .build()
  }

  override def aggregateTags = KeeperEvent.Tag.allTags

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userRoles (
          userId UUID,
          roles text,
          PRIMARY KEY (userId)
          )
        """)
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userPermissions (
          userId UUID,
          permissions text,
          PRIMARY KEY (userId)
          )
        """
      )
      _ <- session.executeCreateTable(
        """
            CREATE TABLE IF NOT EXISTS userAuthToken (
            userId UUID,
            authToken text,
            PRIMARY KEY (userId)
            )
          """)

    } yield Done
  }

  private def preparedStatements() = {
    for {
      insertRoles <- session.prepare(
        """
          INSERT INTO userRoles(userId, roles) VALUES (?,?)
        """)
      insertPermissions <- session.prepare(
        """
          INSERT INTO userPermissions (userId, permissions) VALUES (?,?)
        """)
      insertAuthToken <- session.prepare(
        """
          INSERT INTO userAuthToken (userId, authToken) VALUES (?,?)
        """)
    } yield {
      insertUserRoles = insertRoles
      insertUserPermissions = insertPermissions
      insertAuthKeyStatement = insertAuthToken
      Done
    }
  }

  private def insertDefaultRolesAndPermissions(userId: UUID) = {
    Future.successful(List(insertDefaultRole(userId), inserDefaultPermission(userId)))
  }

  private def insertDefaultRole(userId: UUID) = insertUserRoles.bind(userId, "USER")

  private def inserDefaultPermission(userId: UUID) = insertUserPermissions.bind(userId, "message.add")

  private def removeToken = ???

  private def insertToken(userId: UUID) = ???

  private def removeUser = ???
}
