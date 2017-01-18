package com.livelygig.product.keeper.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.security.resource.ResourceServerSecurity

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by shubham.k on 09-01-2017.
  */
class KeeperServiceImpl(registry: PersistentEntityRegistry, keeperRepo: KeeperRepository)(implicit ec: ExecutionContext) extends KeeperService  {
  // Return the authorization roles and permission of the subject
  override def authorize() = ???

  override def login() =  ServiceCall{ loginModel=>
    keeperRepo.searchForUsernameOrEmail(loginModel).map{
      case Some(userId) => {
        refFor(userId).ask(LoginUser(loginModel.password))
        ""
      }
      case None => throw NotFound("User not found")
    }
//    refFor(UUID.fromString("userId") ).ask(LoginUser(loginModel.password))

  }

  override def createUser() = ???

  private def refFor(userId: UUID) = registry.refFor[KeeperEntity](userId.toString)
//  private
}
