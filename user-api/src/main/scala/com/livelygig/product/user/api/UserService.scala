package com.livelygig.product.user.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait UserService extends Service {

  def sayHello: ServiceCall[NotUsed, String]

  def descriptor = {
    import Service._
    named("user").withCalls(
      pathCall("/api/hello/", sayHello)
    )
  }
}

