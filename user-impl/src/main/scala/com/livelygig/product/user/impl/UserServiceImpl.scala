package com.livelygig.product.user.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.user.api.{User, UserService}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(registry: PersistentEntityRegistry,cassandraSession: CassandraSession, userRepository: UserRepository)(implicit ec: ExecutionContext, mat: Materializer) extends UserService {


  override def login = ServiceCall { user =>
    userRepository.getUser(user)
  }

  override def signup = ServiceCall { user =>
    userRepository.doesUserExists(user).flatMap{
      case true => throw Forbidden("User already exists")
      case false =>
        val uuid = UUID.randomUUID()
        refFor(uuid).ask(CreateUser(User(user.id,user.email,user.password,user.name))).map{
          _ =>  user.copy(id = uuid, password = "")
        }
    }
  }
  /*override def signup = ServiceCall { user =>
    Future("yo")
  }*/

  private def refFor(id:UUID) = registry.refFor[UserEntity](id.toString)
}
