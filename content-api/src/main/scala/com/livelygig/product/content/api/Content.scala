package com.livelygig.product.content.api

import java.util._

import play.api.libs.json.{Format, Json}

/**
 * Created by shubham.k on 21-12-2016.
 */
case class Content(content: String, posttime: Date)

object Content {
  implicit val format: Format[Content] = Json.format
}
