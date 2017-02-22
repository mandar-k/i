package com.livelygig.product.keeper.api

import com.livelygig.product.keeper.api.models.UserProfile
import julienrf.json.derived
import play.api.libs.json.{Format, Json, __}

/**
 * Created by shubham.k on 01-02-2017.
 */
sealed trait KeeperEventsForTopics {
  val userUri: String
}

case class UserCreated(userUri: String, email: String, userProfile: UserProfile, authToken: String, userName: String) extends KeeperEventsForTopics

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

case class UserActivated(userUri: String) extends KeeperEventsForTopics

object UserActivated {
  implicit val format: Format[UserActivated] = Json.format
}

object KeeperEventsForTopics {
  implicit val format: Format[KeeperEventsForTopics] =
    derived.flat.oformat((__ \ "type").format[String])
}

