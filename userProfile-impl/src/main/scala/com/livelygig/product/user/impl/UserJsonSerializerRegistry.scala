package com.livelygig.product.user.impl
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.livelygig.product.user.api.User

object UserJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[User],
    JsonSerializer[LoginUser],
    JsonSerializer[CreateUser],
    JsonSerializer[UserCreated]
  )
}