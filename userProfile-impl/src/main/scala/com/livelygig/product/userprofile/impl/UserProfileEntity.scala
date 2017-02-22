package com.livelygig.product.userprofile.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.userprofile.api.models.UserProfileResponse

/**
 * Created by shubham.k on 16-12-2016.
 */
class UserProfileEntity extends PersistentEntity {

  override type Command = UserProfileCommand

  override type Event = UserProfileEvent

  override type State = UserProfileState

  override def initialState = UserProfileState.initialState

  override def behavior = {
    case UserProfileState(_, UserProfileStatus.DoesNotExist) => doesNotExists
    case UserProfileState(Some(_), UserProfileStatus.Activated) => userActivated
    case UserProfileState(Some(_), UserProfileStatus.NotActivated) => userNotActivated
    case UserProfileState(Some(_), UserProfileStatus.Disabled) => userDisabled
    case UserProfileState(Some(_), UserProfileStatus.Deleted) => userDeleted

  }

  def doesNotExists = {
    Actions()
      .onCommand[CreateProfile, Done] {
        case (CreateProfile(user), ctx, _) =>
          ctx.thenPersist(UserProfileCreated(user))(_ => ctx.reply(Done))
      }.onEvent {
        case (UserProfileCreated(user), state) => {
          state.copy(state = Some(user)).changeStatus(UserProfileStatus.NotActivated)
        }
      }
  }

  def userActivated = {
    Actions()
      .onCommand[CreateProfile, Done] {
        case (CreateProfile(user), ctx, _) =>
          ctx.thenPersist(UserProfileCreated(user))(_ => ctx.reply(Done))
      }
      .onReadOnlyCommand[GetProfile.type, UserProfileResponse] {
        case (GetProfile, ctx, state) => {
          ctx.reply(UserProfileResponse("userProfileResponse", state.state.get))
        }
      }
      .onEvent {
        case (UserProfileCreated(user), state) => {
          state.copy(state = Some(user)).changeStatus(UserProfileStatus.NotActivated)
        }
      }
  }

  def userNotActivated = {
    Actions()
      .onCommand[ActivateUserProfile.type, Done] {
        case (ActivateUserProfile, ctx, _) => ctx.thenPersist(UserProfileActivated)(_ => ctx.reply(Done))
      }
      .onEvent {
        case (UserProfileActivated, state) => state.copy(userStatus = UserProfileStatus.Activated)
      }
  }

  def userDisabled = ???

  def userDeleted = ???
}
