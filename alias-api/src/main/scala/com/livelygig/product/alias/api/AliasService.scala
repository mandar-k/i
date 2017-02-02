package com.livelygig.product.alias.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

import com.livelygig.product.security.keeper.SecurityHeaderFilter

/**
  * Created by shubham.k on 02-02-2017.
  */
trait AliasService extends Service {
  def addAlias(): ServiceCall[String, String]

  def descriptor = {
    import Service._
    named("alias").withCalls(
      namedCall("/api/alias/add", addAlias _)
    )
  }
}
