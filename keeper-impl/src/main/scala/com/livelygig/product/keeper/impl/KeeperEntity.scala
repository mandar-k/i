package com.livelygig.product.keeper.impl

import java.util.{Date, UUID}

import akka.Done
import com.lightbend.lagom.scaladsl.api.transport.Forbidden
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.keeper.impl.models.UserLoginInfo


/**
  * Created by shubham.k on 10-01-2017.
  */
class KeeperEntity extends PersistentEntity {

  override type Command = KeeperCommand

  override type Event = KeeperEvent

  override type State = KeeperState

  override def initialState = KeeperState.initialState

  override def behavior = {
    case KeeperState(_, UserStatus.DoesNotExist) => doesNotExists
    case KeeperState(Some(userauth), UserStatus.Activated) => userActivated
    case KeeperState(Some(userauth), UserStatus.NotActivated) => userNotActivated
    case KeeperState(Some(userauth), UserStatus.Disabled) => userDisabled
    case KeeperState(Some(userauth), UserStatus.Deleted) => userDeleted

  }

  def doesNotExists = ???/*{
    Actions()
      .onCommand[CreateUser, Done]
  }*/

  def userActivated = {
    Actions()
      .onCommand[LoginUser, String] {
      case (LoginUser(password), ctx, userState) =>
        if (password == userState.state.get.password) {
          val authKey = UUID.randomUUID().toString
          val userLoginInfo = UserLoginInfo(new Date(), userState.state.get.email, authKey, userState.state.get.userId)
          ctx.thenPersist(UserLogin(userLoginInfo),_ => ctx.reply(authKey))
        }
        else {
          val loginFailedInfo = new UserLoginInfo(new Date(), userState.state.get.email, "", userState.state.get.userId)
          ctx.thenPersist(UserLogin(loginFailedInfo),_ => ctx.commandFailed(throw Forbidden("Authorization failed")))
        }
    }
  }

  def userNotActivated = ???

  def userDisabled = ???

  def userDeleted = ???
}
