package com.livelygig.product.userprofile.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.userprofile.api.User
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait UserCommand

case class CreateUser(user: User) extends UserCommand with ReplyType[Done]
object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}
case class LoginUser(user:User) extends UserCommand with ReplyType[Option[User]]
object LoginUser  {
  implicit val format: Format[LoginUser] = Json.format
}
