package com.livelygig.product.user.impl

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{Jsonable, SerializerRegistry, Serializers}
import com.livelygig.product.user.api.User
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
    case None => noUser
  }

  private val noUser = {
    Actions()
      .onReadOnlyCommand[LoginUser.type, Option[User]] {
      case (LoginUser, ctx, state) => ctx.reply(state)
    }
      .onCommand[CreateUser, Done] {
      case (CreateUser(user), ctx, state) =>
        ctx.thenPersist(UserCreated(user), _ => ctx.reply(Done))
    }
      .onEvent{
        case (UserCreated(user), state) => Some(user)
      }
  }
}










