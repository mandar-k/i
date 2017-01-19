package com.livelygig.product.keeper.impl

import com.lightbend.lagom.scaladsl.playjson.{SerializerRegistry, Serializers}
import com.livelygig.product.keeper.api.models.{User, UserAuth, UserProfile}

/**
  * Created by shubham.k on 13-01-2017.
  */
class KeeperSerializerRegistry extends SerializerRegistry {
  override def serializers = List(

    //User
    Serializers[User],
    Serializers[UserAuth],
    Serializers[UserProfile],

    // State
    Serializers[KeeperState],

    // Commands and replies.type
    Serializers[LoginUser],
    Serializers[CreateUser],
    Serializers[DeleteUser],

    // Events
    Serializers[UserCreated],
    Serializers[UserLogin],
    Serializers[UserDisabled],
    Serializers[UserDeleted],
    Serializers[TokenCreated],
    Serializers[TokenDeleted]

  )
}
