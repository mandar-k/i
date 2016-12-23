package com.livelygig.product.user.impl

import java.util.UUID

import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Forbidden
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.user.api.UserService
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.ExecutionContext

class UserServiceImpl(registry: PersistentEntityRegistry,cassandraSession: CassandraSession, userRepository: UserRepository)(implicit ec: ExecutionContext, mat: Materializer) extends UserService {


  override def login = ServiceCall { user =>
    refFor(user.email).ask(LoginUser(user)).map{
      case None => throw Forbidden("Authorization failed.")
      case Some(ur) => user.copy(password = "")
    }
  }

  override def signup = ServiceCall { user =>
    val userId = UUID.randomUUID()
    refFor(user.email).ask(CreateUser(user.copy(id = userId))).map(_ => null)
  }

  private def refFor(email:String) = registry.refFor[UserEntity](email)
}
