package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object UserProfileJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[UserProfileCreated]
  )
}