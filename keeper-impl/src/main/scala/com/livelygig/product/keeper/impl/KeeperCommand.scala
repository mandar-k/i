package com.livelygig.product.keeper.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.keeper.api.models.{User, UserAuth}
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 11-01-2017.
  */
trait KeeperCommand extends Jsonable

case class LoginUser(password: String) extends KeeperCommand with ReplyType[String]

object LoginUser {
  implicit val format: Format[LoginUser] = Json.format
}

case class DeleteToken(userAuth: UserAuth) extends KeeperCommand with ReplyType[String]

object DeleteToken {
  implicit val format: Format[LoginUser] = Json.format
}

case class CreateUser(user: User) extends KeeperCommand with ReplyType[Done]

object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}

case class DeleteUser(userAuth: UserAuth) extends KeeperCommand with ReplyType[Done]

object DeleteUser {
  implicit val format: Format[DeleteUser] = Json.format
}
