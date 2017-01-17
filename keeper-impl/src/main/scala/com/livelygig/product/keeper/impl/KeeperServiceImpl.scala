package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.security.resource.ResourceServerSecurity

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 09-01-2017.
  */
class KeeperServiceImpl(implicit ec: ExecutionContext) extends KeeperService  {
  // Return the authorization roles and permission of the subject
  override def authorize() = ???

  override def login() =  ServiceCall{ req=>

    Future("")
  }

  override def createUser() = ???
}
