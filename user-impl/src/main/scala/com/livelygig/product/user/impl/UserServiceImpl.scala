package com.livelygig.product.user.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.user.api.UserService
import com.livelygig.product.user.api

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(registry: PersistentEntityRegistry, system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) extends UserService {

  override def login = ServiceCall { user =>
    refFor(user.email).ask(LoginUser).map {
      case Some(user) =>
        "Ok"
      case None =>
        throw NotFound(s"User with email ${user.email}")
    }
  }


  private def refFor(userEmail:String) = registry.refFor[UserEntity](userEmail)
}
