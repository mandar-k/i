package com.livelygig.product.emailnotifications.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializerRegistry}

/**
  * Created by shubham.k on 25-01-2017.
  */
object EmailNotificationJsonSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = Nil
}
