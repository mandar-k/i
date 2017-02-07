package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.livelygig.product.keeper.api.models.{User, UserAuth, UserProfile}

/**
  * Created by shubham.k on 13-01-2017.
  */
object KeeperJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(

    //User
    JsonSerializer[User],
    JsonSerializer[UserAuth],
    JsonSerializer[UserProfile],

    // State
    JsonSerializer[KeeperState],

    // Commands and replies.type
    JsonSerializer[LoginUser],
    JsonSerializer[CreateUser],
    JsonSerializer[DeleteUser],

    // Events
    JsonSerializer[UserCreated],
    JsonSerializer[UserLogin],
    JsonSerializer[UserDisabled],
    JsonSerializer[UserDeleted],
    JsonSerializer[TokenCreated],
    JsonSerializer[TokenDeleted],
    JsonSerializer[UserActivated]

  )
}
