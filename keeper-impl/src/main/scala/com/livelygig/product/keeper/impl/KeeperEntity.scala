package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.persistence._


/**
  * Created by shubham.k on 10-01-2017.
  */
class KeeperEntity extends PersistentEntity{

  override  type Command = KeeperCommand

  override type Event = KeeperEvent

  override type State = KeeperState

  override def initialState = KeeperState.initialState

  override def behavior = {
    case KeeperState(_, UserStatus.DoesNotExist) => doesNotExists
    case KeeperState(Some(userauth), UserStatus.Activated)    => userActivated
    case KeeperState(Some(userauth), UserStatus.NotActivated) => userNotActivated
    case KeeperState(Some(userauth), UserStatus.Disabled)     => userDisabled
    case KeeperState(Some(userauth), UserStatus.Deleted)      => userDeleted

  }

  def doesNotExists = ???

  def userActivated = ???

  def userNotActivated = ???

  def userDisabled = ???

  def userDeleted = ???
}
