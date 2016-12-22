package com.livelygig.product.user.impl

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence._
import com.lightbend.lagom.scaladsl.playjson.Jsonable
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

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



object UserEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserEvent](NumShards)
}

sealed trait UserEvent extends AggregateEvent[UserEvent] with Jsonable {
  override def aggregateTag: AggregateEventShards[UserEvent] = UserEvent.Tag
}


case class UserLoggedIn(id: String) extends UserEvent

object UserLoggedIn {
  implicit val format: Format[UserLoggedIn] = Json.format
}

case class User(id: UUID, email:String, password: String, name:String)

object User {
  implicit val format: Format[User] = Json.format

}

case class UserCreated(user: User) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

sealed trait UserCommand extends Jsonable

case class CreateUser(user: User) extends UserCommand with ReplyType[Done]
object CreateUser {
  implicit val format: Format[CreateUser.type] = singletonFormat(CreateUser)
}

case object LoginUser extends UserCommand with ReplyType[Option[User]] {
  implicit val format: Format[LoginUser.type] = singletonFormat(LoginUser)
}



