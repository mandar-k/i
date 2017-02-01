package com.livelygig.product.keeper.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{ErrorResponse, UserAuthRes}
import com.livelygig.product.keeper.impl.models.MsgTypes
import com.livelygig.product.security.resource.ResourceServerSecurity

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 09-01-2017.
  */
class KeeperServiceImpl(registry: PersistentEntityRegistry, keeperRepo: KeeperRepository)(implicit ec: ExecutionContext) extends KeeperService {
  // Return the authorization roles and permission of the subject
  override def authorize() = ???

  override def login() = ServiceCall { loginModel =>

    for {
      userId <- keeperRepo.searchForUsernameOrEmail(loginModel)
      res <- userId match {
        case Some(uid) => refFor(uid).ask(LoginUser(loginModel.password))
        case None => Future.successful(UserAuthRes(MsgTypes.AUTH_ERROR, ErrorResponse("Authentication Failed")))
      }
    } yield res
  }


  override def createUser() = ServiceCall { userModel =>
    for {
      userFromEmail <- keeperRepo.searchForEmail(userModel.userAuth.email)
      userFromUserName <- userFromEmail match {
        case Some(usr) => Future.successful(Some(UserAuthRes(MsgTypes.CREATE_USER_ERROR,ErrorResponse("Email already registered"))))
        case None => {
          keeperRepo.searchForUsername(userModel.userAuth.username).map{
            case Some(u) => Some(UserAuthRes(MsgTypes.CREATE_USER_ERROR,ErrorResponse("Username already registered")))
            case None => None
          }
        }
      }
      reply <- userFromUserName match {
        case Some(u) => Future.successful(u)
        case None => {
          val uid = UUID.randomUUID()
          refFor(uid).ask(CreateUser(userModel))
        }
      }
    } yield reply
  }

  override def keeperTopicProducer = ???


  private def refFor(userId: UUID) = registry.refFor[KeeperEntity](userId.toString)

  //  private
}
