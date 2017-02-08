package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.livelygig.product.userprofile.api.User

object UserProfileJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[User],
    JsonSerializer[LoginUser],
    JsonSerializer[CreateUser],
    JsonSerializer[UserCreated]
  )
}