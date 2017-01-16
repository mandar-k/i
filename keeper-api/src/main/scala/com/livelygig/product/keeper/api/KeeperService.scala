package com.livelygig.product.keeper.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.livelygig.product.keeper.api.models.AuthorizationInfo

/**
  * Created by shubham.k on 09-01-2017.
  */
trait KeeperService extends Service {

  def authorize(): ServiceCall[String,AuthorizationInfo]

   def descriptor = {
     import Service._
     named("authorization").withCalls(
       namedCall("/api/auth/authorize", authorize _)
     )
   }
}
