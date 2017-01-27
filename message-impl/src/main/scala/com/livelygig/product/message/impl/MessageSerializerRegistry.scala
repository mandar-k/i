package com.livelygig.product.message.impl
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.livelygig.product.message.api.Message

object MessageJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[Message],
    JsonSerializer[AddMessage],
    JsonSerializer[MessagePosted]
  )
}