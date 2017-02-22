package com.livelygig.product.keeper.impl.models

/**
 * Created by shubham.k on 30-01-2017.
 */
object MsgTypes {
  // user is awaiting confirmation
  val CREATE_USER_WAITING = "createUserWaiting"
  // user already exists
  val CREATE_USER_ERROR = "createUserError"
  val INITIALIZE_SESSION_RESPONSE = "initializeSessionResponse"
  // error due to authentication failes
  val AUTH_ERROR = "authenticationError"
  // woops!!! user does not exists
  val USER_NOT_FOUND = "userNotFound"

  val ACCOUNT_ACTIVATED = "accountActivated"

  val INVALID_ACTIVATION_TOKEN = "activationTokenInvalid"
}
