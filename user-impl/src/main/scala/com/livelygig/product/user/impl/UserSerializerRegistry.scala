package com.livelygig.product.user.impl
import com.lightbend.lagom.scaladsl.playjson.{SerializerRegistry, Serializers}

class UserSerializerRegistry extends SerializerRegistry {
  override def serializers = List(
    Serializers[User],
    Serializers[UserLoggedIn],
    Serializers[LoginUser.type ]
  )
}