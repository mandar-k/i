package com.livelygig.product.alias.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 07-02-2017.
  */
trait AliasCommand

case class AddDefaultAlias (agentUri: String, username: String) extends AliasCommand with ReplyType[Done]

object AddDefaultAlias {
  implicit val format: Format[AddDefaultAlias] = Json.format
}
