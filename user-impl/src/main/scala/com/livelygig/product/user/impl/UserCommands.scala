package com.livelygig.product.user.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.user.api.User
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.Format

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait UserCommand extends Jsonable

case class CreateUser(user: User) extends UserCommand with ReplyType[Done]
object CreateUser {
  implicit val format: Format[CreateUser.type] = singletonFormat(CreateUser)
}

case object LoginUser extends UserCommand with ReplyType[Option[User]] {
  implicit val format: Format[LoginUser.type] = singletonFormat(LoginUser)
}
