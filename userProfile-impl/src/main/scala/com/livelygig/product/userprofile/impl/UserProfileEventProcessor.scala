
package com.livelygig.product.userprofile.impl

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by shubham.k on 28-12-2016.
 */
private[impl] class UserProfileEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
    extends ReadSideProcessor[UserProfileEvent] {
  private var insertUserAliasStatement: PreparedStatement = null

  override def buildHandler() = {
    readSide.builder[UserProfileEvent]("UserEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => preparedStatements())
      //            .setEventHandler[UserProfileCreated](e => insertDefaultUserAlias(e.entityId, e.event.userProfile.username))
      .build
  }

  def aggregateTags = UserProfileEvent.Tag.allTags

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
        CREATE TABLE IF NOT EXISTS UserAliases (
          userUri text PRIMARY KEY,
          aliasUri text,
          aliasLabel text,
          isDefault boolean
        )
      """
      )
    } yield {
      Done
    }

  }

  private def preparedStatements() = {
    for {
      insertUserAlias <- session.prepare(
        """
          INSERT INTO UserAliases (
          userUri,
          aliasUri,
          aliasLabel,
          isDefault
        ) VALUES (?, ?, ?, ?)
        """
      )
    } yield {
      insertUserAliasStatement = insertUserAlias
      Done
    }
  }

  private def insertDefaultUserAlias(agentUri: String, userName: String) = {
    val defaultAliasUri = agentUri + s"/$userName"
    Future(List(insertUserAliasStatement.bind(agentUri, defaultAliasUri, userName, true.asInstanceOf[java.lang.Boolean])))
  }

}
