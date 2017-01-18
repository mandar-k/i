package com.livelygig.product.keeper.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import com.livelygig.product.keeper.api.models.{UserAuth, UserLoginModel}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 11-01-2017.
  */
private [impl] class KeeperRepository(session: CassandraSession)(implicit ec: ExecutionContext) {

  def searchForUsernameOrEmail(userLoginModel: UserLoginModel) =  {
    for {
      uid <- searchForUsername(userLoginModel.usernameOrEmail)
      userId <- if (!uid.isEmpty) Future.successful(uid) else searchForEmail(userLoginModel.usernameOrEmail)
    } yield userId
  }

  def searchForUsername(username:String):Future[Option[UUID]] = {
    session.selectOne(
      """
        SELECT FROM userIdByUsername WHERE userName = ?
      """, username).map{
      case Some(row) => Some(row.getUUID("userId"))
      case None => None
    }
  }

  def searchForEmail(email:String):Future[Option[UUID]] = {
    session.selectOne(
      """
        SELECT FROM userIdByEmail WHERE email = ?
      """, email).map{
      case Some(row) => Some(row.getUUID("userId"))
      case None => None
    }
  }
}
