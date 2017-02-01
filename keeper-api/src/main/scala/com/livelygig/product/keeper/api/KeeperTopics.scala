package com.livelygig.product.keeper.api

import java.util.UUID

import com.livelygig.product.keeper.api.models.UserProfile
import julienrf.json.derived
import play.api.libs.json.{Format, Json, __}

/**
  * Created by shubham.k on 01-02-2017.
  */
sealed trait KeeperTopics {
  val userID:UUID
}

case class AddUserProfile(userID: UUID, userProfile: UserProfile) extends KeeperTopics

object AddUserProfile {
  implicit val format: Format[AddUserProfile] = Json.format
}

case class AddDefaultAlias(userID: UUID) extends KeeperTopics

object AddDefaultAlias {
  implicit val format: Format[AddDefaultAlias] = Json.format
}

case class SendActivationLink(userID:UUID, email:String, activationCode: String) extends KeeperTopics

object SendActivationLink {
  implicit val format: Format[SendActivationLink] = Json.format
}

object KeeperTopics {
  implicit val format: Format[KeeperTopics] =
    derived.flat.oformat((__ \ "type").format[String])
}


