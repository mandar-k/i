package com.livelygig.product.user.impl
import com.lightbend.lagom.scaladsl.playjson.{SerializerRegistry, Serializers}
import com.livelygig.product.user.api.User

class UserSerializerRegistry extends SerializerRegistry {
  override def serializers = List(
    Serializers[User],
    Serializers[LoginUser.type ],
    Serializers[UserCreated]
  )
}