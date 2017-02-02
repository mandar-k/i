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
import com.livelygig.product.keeper.api.models.{ErrorResponse, UserAuthRes}
import com.livelygig.product.keeper.impl.models.MsgTypes
import com.livelygig.product.security.resource.ResourceServerSecurity
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.livelygig.product.keeper.api

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
        case Some(uid) => registry.refFor[KeeperEntity](uid).ask(LoginUser(loginModel.password))
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
          val rand = SecureRandom.getInstanceStrong()
          val bytes = new Array[Byte](20)
          // Generate random Agent URI
          rand.nextBytes(bytes)
          val uri = new URI("agent://" + bytes.map("%02X" format _).mkString)
          registry.refFor[KeeperEntity](uri.toString).ask(CreateUser(userModel))
        }
      }
    } yield reply
  }

  override def activateAccount() = ???

  override def keeperTopicProducer = TopicProducer.taggedStreamWithOffset(KeeperEvent.Tag.allTags.toList) {(tag, offset) =>
    registry.eventStream(tag, offset)
      .filter{
        _.event match {
          case x@(_: UserCreated) => true
          case _ => false
        }
      }.mapAsync(1)(convertEvents)

  }


  private def convertEvents(eventStreamElement: EventStreamElement[KeeperEvent]): Future[(KeeperEventsForTopics, Offset)] = {
    eventStreamElement match {
      case EventStreamElement(userUri, UserCreated(user, activationToken), offset) =>
        Future.successful{
          (api.UserCreated(userUri,user.userAuth.email,user.userProfile,activationToken), offset)
        }
    }
  }


  //  private
}
