package com.livelygig.product.content.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object MessageJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    // commands
    JsonSerializer[AddContent],
    JsonSerializer[GetContent.type],
    // events
    JsonSerializer[ContentPosted]
  )
}