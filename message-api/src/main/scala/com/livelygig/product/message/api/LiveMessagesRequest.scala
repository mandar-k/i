package com.livelygig.product.message.api

import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 28-12-2016.
  */
case class LiveMessagesRequest(userIds: Seq[String])

object LiveMessagesRequest {
  implicit val format: Format[LiveMessagesRequest] = Json.format
}
