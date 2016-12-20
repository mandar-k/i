package com.livelygig.product.user.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait UserService extends Service {

  def login: ServiceCall[User, String]

  def descriptor = {
    import Service._
    named("user").withCalls(
//      restCall(Method.POST,"/api/login/", login)
      pathCall("/api/login/", login)
    )
  }
}

case class User(email:String, password: String)

object User {
  implicit val format: Format[User] = Json.format

}
