package com.livelygig.product.keeper.impl

import java.util.UUID

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.livelygig.product.keeper.api.models.{User, UserAuth}
import com.livelygig.product.keeper.impl.models.UserLoginInfo
import org.joda.time.DateTimeZone
import play.api.Configuration

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 11-01-2017.
  */
private[impl] class KeeperEventProcessor(session: CassandraSession, readSide: CassandraReadSide, config: Configuration)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[KeeperEvent] {
  private val accessTokenExpire = config.getMilliseconds("authentication.tokenExpire").getOrElse(60 * 60L * 24 * 1000) / 1000
  private val activationTokenExpire = config.getMilliseconds("activation.tokenExpire").getOrElse(60 * 60L * 24 * 1000) / 1000
  private var insertAuthKeyStatement: PreparedStatement = _
  private var insertActivationTokenStatement: PreparedStatement = _
  private var deleteAuthKeyStatement: PreparedStatement = _
  private var deleteActivationToken: PreparedStatement = _
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
      .setEventHandler[UserLogin](e => insertAuthToken(e.entityId, e.event.userLoginInfo))
      .setEventHandler[UserCreated](e => insertUserAuthDetails(e.entityId, e.event.user, e.event.activationToken))
      .setEventHandler[UserActivated](e => removeToken(e.event.tokenToDelete))
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
          userUri text,
          roles text,
          PRIMARY KEY (userUri)
          )
        """)
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userPermissions (
          userUri text,
          permissions text,
          PRIMARY KEY (userUri)
          )
        """
      )
      _ <- session.executeCreateTable(
        """
            CREATE TABLE IF NOT EXISTS userAuthToken (
            userUri text,
            authToken text,
            PRIMARY KEY (userUri)
            )
          """)
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userIdByUsername (
          username text,
          userUri text,
          PRIMARY KEY (username)
          )
        """)
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userIdByEmail (
          email text,
          userUri text,
          PRIMARY KEY (email)
          )
        """
      )
      _ <- session.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS userActivationToken (
          activationToken text,
          userUri text,
          PRIMARY KEY (activationToken)
          )
        """
      )

    } yield Done
  }

  private def preparedStatements() = {
    for {
      insertRoles <- session.prepare(
        """
          INSERT INTO userRoles(userUri, roles) VALUES (?,?)
        """)
      insertPermissions <- session.prepare(
        """
          INSERT INTO userPermissions (userUri, permissions) VALUES (?,?)
        """)
      insertAuthToken <- session.prepare(
        """
          INSERT INTO userAuthToken (userUri, authToken) VALUES (?,?) USING TTL ?
        """)
      insertActivationToken <- session.prepare(
        """
          INSERT INTO userActivationToken (userUri, activationToken) VALUES (?,?) USING TTL ?
        """)
      insertUsernameAndUserId <- session.prepare(
        """
           INSERT INTO userIdByUsername (username, userUri) VALUES (?,?)
        """
      )
      insertEmailAndUserId <- session.prepare(
        """
           INSERT INTO userIdByEmail (email, userUri) VALUES (?,?)
        """
      )
      deleteActivation <- session.prepare(
        """
          DELETE FROM userActivationToken where activationToken = ?
        """
      )
    } yield {
      insertUserRoles = insertRoles
      insertUserPermissions = insertPermissions
      insertAuthKeyStatement = insertAuthToken
      insertEmailUserId = insertEmailAndUserId
      insertUsernameUserId = insertUsernameAndUserId
      insertActivationTokenStatement = insertActivationToken
      deleteActivationToken = deleteActivation
      Done
    }
  }

  private def insertUserAuthDetails(userId: String, user: User, activationToken: String) = {
    Future.successful(
      List(
        insertUserRoles.bind(userId, "USER"),
        insertUserPermissions.bind(userId, "message.add"),
        insertUsernameUserId.bind(user.userAuth.email, userId),
        insertUsernameUserId.bind(user.userAuth.username, userId),
        insertActivationTokenStatement.bind(userId, activationToken, activationTokenExpire.toInt.asInstanceOf[java.lang.Integer])
      ))
  }

  private def removeToken(tokenToRemove: String) = {
    Future.successful(List(deleteActivationToken.bind(tokenToRemove)))
  }

  private def insertAuthToken(userID: String, userLoginInfo: UserLoginInfo) = {
    // FIXME type conversion from long to integer
    Future.successful(List(insertAuthKeyStatement.bind(userID, userLoginInfo.authKeyGenerated, accessTokenExpire.toInt.asInstanceOf[java.lang.Integer])))
  }

  private def removeUser = ???
}
