package com.livelygig.product.user.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{Jsonable, SerializerRegistry, Serializers}
import play.api.libs.json.{Format, Json}
import com.livelygig.product.utils.JsonFormats._

/**
  * Created by shubham.k on 16-12-2016.
  */
class UserEntity extends PersistentEntity{
  override type Command = UserCommand

  override type Event = UserEvent

  override type State = Option[User]

  override def initialState = None

  override def behavior = {
    case None =>
      Actions().onReadOnlyCommand[LoginUser.type, Option[User]] {
        case (LoginUser, ctx, state) => ctx.reply(state)
      }
  }
}

case class User(email:String, password: String) extends Jsonable

object User {
  implicit val format:Format[User] = Json.format
}

sealed trait UserCommand extends Jsonable


sealed trait UserEvent extends Jsonable

case class UserLogin(name: String) extends UserEvent

object UserLogin {
  implicit val format: Format[UserLogin] = Json.format
}

case object LoginUser extends UserCommand with ReplyType[Option[User]] {
  implicit val format: Format[LoginUser.type] = singletonFormat(LoginUser)
}

class UserSerializerRegistry extends SerializerRegistry {
  override def serializers = List(
    Serializers[User],
    Serializers[UserLogin],
    Serializers[LoginUser.type ]
  )
}

