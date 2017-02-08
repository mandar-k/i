package com.livelygig.product.userprofile.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.userprofile.api.User

/**
  * Created by shubham.k on 16-12-2016.
  */
class UserProfileEntity extends PersistentEntity{
  override type Command = UserCommand

  override type Event = UserEvent

  override type State = Option[User]

  override def initialState = None

  override def behavior = {
    case None => noUser
    case Some(user) => userFound
  }

  private val noUser = {
    Actions()
      .onReadOnlyCommand[LoginUser, Option[User]] {
      case (LoginUser(user), ctx, state) => ctx.invalidCommand("User Does not exist.")
    }
      .onCommand[CreateUser, Done] {
      case (CreateUser(user), ctx, state) =>{
        ctx.thenPersist(UserCreated(user))(_ => ctx.reply(Done))
      }
    }
      .onEvent{
        case (UserCreated(user), state) => Some(user)
      }
  }

  private val userFound = {
    Actions()
      .onReadOnlyCommand[CreateUser, Done] {
      case (_, ctx, _) => ctx.invalidCommand("This email is already registered.")
    }
      .onReadOnlyCommand[LoginUser , Option[User]] {
      case (LoginUser(user), ctx, state) =>
        if (state.get.password == user.password)
          ctx.reply(state)

        else
          ctx.invalidCommand("Authorization failed.")
    }
  }
}
