package com.livelygig.product.keeper.impl

import java.util.{Date, UUID}

import akka.Done
import com.lightbend.lagom.scaladsl.api.transport.Forbidden
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.keeper.api.models.{CreateUserResponse, ErrorResponse, InitializeSessionResponse, UserAuthRes}
import com.livelygig.product.keeper.impl.models.{MsgTypes, UserLoginInfo}
import com.livelygig.product.keeper.api.models.{User, UserAuth, UserProfile}
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

  def doesNotExists = {
    Actions()
      .onCommand[CreateUser, UserAuthRes] {
      case (CreateUser(user), ctx, _) =>
        // TODO use srp to secure the user login details
        ctx.thenPersist(UserCreated(user))(_ => ctx.reply(UserAuthRes(MsgTypes.CREATE_USER_WAITING, CreateUserResponse(""))))
    }.onEvent{
      case (UserCreated(user), state) => {
        // TODO send activation email using email and notification service and
        // TODO add the user profile on the user profile service
        // TODO add default alias on the different service
        // TODO change default state to not activated
        state.copy(state = Some(user.userAuth), userStatus = UserStatus.Activated)
      }
    }
  }

  def userActivated = {
    Actions()
      .onCommand[LoginUser, UserAuthRes] {
      case (LoginUser(password), ctx, userState) =>
        // TODO use srp to secure the login info
        if (password == userState.state.get.password) {
          val authKey = UUID.randomUUID().toString
          val userLoginInfo = UserLoginInfo(new Date(), userState.state.get.email, authKey)
          ctx.thenPersist(UserLogin(userLoginInfo))(_ => ctx.reply(UserAuthRes(MsgTypes.INITIALIZE_SESSION_RESPONSE, InitializeSessionResponse(authKey))))
        }
        else {
          ctx.thenPersist(UserLoginFailed(userState.state.get.email,"Authentication error"))(_ => ctx.reply(UserAuthRes(MsgTypes.AUTH_ERROR, ErrorResponse("Authentication Failed"))))
        }
    }
  }

  def userNotActivated = {
    Actions()
      .onReadOnlyCommand[LoginUser, UserAuthRes] {
      // TODO read response message from conf file
      case (LoginUser(_), ctx, _) => ctx.reply(UserAuthRes(MsgTypes.AUTH_ERROR, ErrorResponse("Please check the activation link in the email sent to you.")))
    }
  }

  def userDisabled = {
    Actions()
      .onReadOnlyCommand[LoginUser, UserAuthRes] {
      // TODO read response message from conf file
      case (LoginUser(_), ctx, _) => ctx.reply(UserAuthRes(MsgTypes.AUTH_ERROR, ErrorResponse("Your account is currently disabled.")))
    }
  }

  def userDeleted = ???
}
