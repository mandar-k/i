package com.livelygig.product.content.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.livelygig.product.content.api.Content

object MessageJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[Content],
    JsonSerializer[AddContent],
    JsonSerializer[ContentPosted]
  )
}