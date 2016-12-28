package com.livelygig.product.user.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait UserService extends Service {

  def login: ServiceCall[User, User]

  def signup: ServiceCall[User, NotUsed]

  def descriptor = {
    import Service._
    named("user").withCalls(
      namedCall("/api/login", login),
      namedCall("/api/signup", signup)

    )
  }
}


