package com.livelygig.product.message.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait MessageService extends Service {

  def getmessages: ServiceCall[NotUsed, NotUsed]


  def descriptor = {
    import Service._
    named("messages").withCalls(
      pathCall("/api/getmessages", getmessages)
    )
  }
}


