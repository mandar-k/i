package com.livelygig.product.alias.impl

import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

/**
  * Created by shubham.k on 25-01-2017.
  */
object AliasJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = Nil
}
