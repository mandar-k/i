package com.livelygig.product.user.impl

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
//import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.user.api.UserService

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(/*registry: PersistentEntityRegistry, */system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) extends UserService {

  override def sayHello = ServiceCall {_ => Future.successful("hello from user service")}


}
