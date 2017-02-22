package com.livelygig.product.content.api

import play.api.libs.json.{Format, Json}

/**
 * Created by shubham.k on 28-12-2016.
 */
case class LiveContentRequest(userIds: Seq[String])

object LiveContentRequest {
  implicit val format: Format[LiveContentRequest] = Json.format
}
