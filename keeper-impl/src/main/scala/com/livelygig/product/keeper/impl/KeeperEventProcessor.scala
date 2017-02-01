package com.livelygig.product.keeper.impl

import java.util.UUID

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.livelygig.product.keeper.api.models.{User, UserAuth}
import com.livelygig.product.keeper.impl.models.UserLoginInfo

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 11-01-2017.
  */
private[impl] class KeeperEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[KeeperEvent] {
  private var insertAuthKeyStatement: PreparedStatement = _
  private var deleteAuthKeyStatement: PreparedStatement = _
  private var insertUserPermissions: PreparedStatement = _
  private var insertUsernameUserId: PreparedStatement = _
  private var insertEmailUserId: PreparedStatement = _
  private var insertUserRoles: PreparedStatement = _
  private var updateUserRoles: PreparedStatement = _
  private var updateUserPermissions: PreparedStatement = _

  override def buildHandler() = {
    readSide.builder[KeeperEvent]("keeperEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(tag => preparedStatements)
      .setEventHandler[UserLogin](e => insertToken( UUID.fromString(e.entityId),e.event.userLoginInfo))
      .setEventHandler[UserCreated](e => insertUserAuthDetails(UUID.fromString(e.entityId),e.event.user))
//      .setEventHandler[UserDeleted](e => removeUser(e.event.user.id))
//      .setEventHandler[TokenCreated](e => )
//      .setEventHandler[TokenDeleted](removeToken)
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
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userIdByUsername (
          username text,
          userId UUID,
          PRIMARY KEY (username)
          )
        """)
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userIdByEmail (
          email text,
          userId UUID,
          PRIMARY KEY (email)
          )
        """
      )

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
      insertUsernameAndUserId <- session.prepare(
        """
           INSERT INTO userIdByUsername (username, userId) VALUES (?,?)
        """
      )
      insertEmailAndUserId <- session.prepare(
        """
           INSERT INTO userIdByEmail (email, userId) VALUES (?,?)
        """
      )
    } yield {
      insertUserRoles = insertRoles
      insertUserPermissions = insertPermissions
      insertAuthKeyStatement = insertAuthToken
      insertEmailUserId = insertEmailAndUserId
      insertUsernameUserId = insertUsernameAndUserId
      Done
    }
  }

  private def insertUserAuthDetails(userId: UUID,user:User) = {
    Future.successful(List(insertDefaultRole(userId), inserDefaultPermission(userId), insertEmailUserID(user,userId), insertUsernameUserID(user,userId)))
  }

  private def insertDefaultRole(userId: UUID) = insertUserRoles.bind(userId, "USER")

  private def inserDefaultPermission(userId: UUID) = insertUserPermissions.bind(userId, "message.add")

  private def insertEmailUserID(user:User, userId: UUID) = insertUsernameUserId.bind(user.userAuth.email,userId)

  private def insertUsernameUserID(user: User, userId: UUID) = insertUsernameUserId.bind(user.userAuth.username, userId)

  private def removeToken = ???

  private def insertToken(userID:UUID,userLoginInfo: UserLoginInfo) = Future.successful(List(insertAuthKeyStatement.bind(userID, userLoginInfo.authKeyGenerated)))

  private def removeUser = ???
}
