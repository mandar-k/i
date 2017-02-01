package com.livelygig.product.keeper.impl

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.keeper.api.models.{User, UserAuth, UserAuthRes}
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 11-01-2017.
  */
trait KeeperCommand

case class LoginUser(userID:UUID,password: String) extends KeeperCommand with ReplyType[UserAuthRes]

object LoginUser {
  implicit val format: Format[LoginUser] = Json.format
}

case class DeleteToken(userAuth: UserAuth) extends KeeperCommand with ReplyType[String]

object DeleteToken {
  implicit val format: Format[LoginUser] = Json.format
}

case class CreateUser(userID:UUID,user: User) extends KeeperCommand with ReplyType[UserAuthRes]

object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}

case class DeleteUser(userAuth: UserAuth) extends KeeperCommand with ReplyType[Done]

object DeleteUser {
  implicit val format: Format[DeleteUser] = Json.format
}
