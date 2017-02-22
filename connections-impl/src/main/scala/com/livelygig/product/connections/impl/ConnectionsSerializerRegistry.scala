package com.livelygig.product.connections.impl

import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

/**
 * Created by shubham.k on 25-01-2017.
 */
object ConnectionsJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = Nil
}
