package com.livelygig.product.user.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.user.api.User
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 21-12-2016.
  */
sealed trait UserEvent extends Jsonable

case class UserLoggedIn(id: String) extends UserEvent

object UserLoggedIn {
  implicit val format: Format[UserLoggedIn] = Json.format
}







case class UserCreated(user: User) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}
