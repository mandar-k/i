package com.livelygig.product.keeper.impl

import java.net.URI
import java.security.SecureRandom
import java.util.UUID

import akka.persistence.query.Offset
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import com.livelygig.product.keeper.api.models.{ErrorResponse, InitializeSessionResponse, UserAuthRes, UserFound}
import com.livelygig.product.keeper.impl.models.MsgTypes
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.livelygig.product.TokenGenerator
import com.livelygig.product.keeper.api
import com.livelygig.product.shared.ResourceServerSecurity

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 09-01-2017.
  */
class KeeperServiceImpl(registry: PersistentEntityRegistry, keeperRepo: KeeperRepository, tokenGenerator: TokenGenerator)(implicit ec: ExecutionContext) extends KeeperService {

  // TODO read response message from conf file

  // Return the authorization roles and permission of the subject
  override def authorize() = ???

  override def login() = ServiceCall { loginModel =>

    for {
      userUri <- keeperRepo.searchForUsernameOrEmail(loginModel)
      res <- userUri match {
        case Some(uri) => registry.refFor[KeeperEntity](uri).ask(LoginUser(loginModel.password))
        case None => Future.successful(UserAuthRes(MsgTypes.AUTH_ERROR, ErrorResponse("Authentication Failed")))
      }
    } yield res
  }

  // TODO think of better way to retreive auth info
  override def getUriFromEmail() = ServiceCall{email =>
    keeperRepo.searchForEmail(email).flatMap{
      case Some(uri) => registry.refFor[KeeperEntity](uri).ask(FindUser(uri))
      case None => Future.successful(UserAuthRes("error", ErrorResponse("Not found")))
    }

  }

  override def createUser() = ServiceCall { userModel =>
    for {
      userFromEmail <- keeperRepo.searchForEmail(userModel.userAuth.email)
      userFromUserName <- userFromEmail match {
        case Some(usr) => Future.successful(Some(UserAuthRes(MsgTypes.CREATE_USER_ERROR, ErrorResponse("Email already registered"))))
        case None => {
          keeperRepo.searchForUsername(userModel.userAuth.username).map {
            case Some(u) => Some(UserAuthRes(MsgTypes.CREATE_USER_ERROR, ErrorResponse("Username already registered")))
            case None => None
          }
        }
      }
      reply <- userFromUserName match {
        case Some(u) => Future.successful(u)
        case None => {
          val agentUriToken = tokenGenerator.generateMD5Token(userModel.userAuth.email)
          val uri = "agent://" + agentUriToken
          registry.refFor[KeeperEntity](uri).ask(CreateUser(userModel))
        }
      }
    } yield reply
  }

  override def activateAccount() = ServiceCall { activationToken =>
    for {
      userUri <- keeperRepo.searchForActivationToken(activationToken)
      res <- userUri match {
        case Some(uri) => registry.refFor[KeeperEntity](uri).ask(ActivateUser(activationToken)).map(e => e)
        case None => Future.successful(UserAuthRes(MsgTypes.INVALID_ACTIVATION_TOKEN, ErrorResponse("Invalid Activation Link.")))
      }
    } yield res
  }

  override def keeperTopicProducer = TopicProducer.taggedStreamWithOffset(KeeperEvent.Tag.allTags.toList) { (tag, offset) =>
    registry.eventStream(tag, offset)
      .filter {
        _.event match {
          case x@(_: UserCreated |_: UserActivated) => true
          case _ => false
        }
      }.mapAsync(1)(convertEvents)

  }


  private def convertEvents(eventStreamElement: EventStreamElement[KeeperEvent]): Future[(KeeperEventsForTopics, Offset)] = {
    eventStreamElement match {
      case EventStreamElement(userUri, UserCreated(user, activationToken), offset) =>
        Future.successful {
          (api.UserCreated(userUri, user.userAuth.email, user.userProfile, activationToken, user.userAuth.username), offset)
        }
      case EventStreamElement(userUri, UserActivated(_), offset) =>
        Future.successful {
          (api.UserActivated(userUri), offset)
        }
    }
  }


  //  private
}
