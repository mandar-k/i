package com.livelygig.product.userprofile.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.livelygig.product.userprofile.api.models.{UserAlias, UserProfile, UserProfileResponse}
import com.livelygig.product.utils.JsonFormats.singletonFormat
import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 23-12-2016.
  */
sealed trait UserProfileCommand

case class CreateProfile(userProfile:UserProfile) extends UserProfileCommand with ReplyType[Done]
object CreateProfile {
  implicit val format: Format[CreateProfile] = Json.format
}

case object ActivateUserProfile extends UserProfileCommand with ReplyType[Done] {
  implicit val format: Format[ActivateUserProfile.type] = singletonFormat(ActivateUserProfile)
}

case object GetProfile extends UserProfileCommand with ReplyType[UserProfileResponse] {
  implicit val format: Format[GetProfile.type ] = singletonFormat(GetProfile)
}