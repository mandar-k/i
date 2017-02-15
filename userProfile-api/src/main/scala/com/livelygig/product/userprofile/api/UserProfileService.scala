package com.livelygig.product.userprofile.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.livelygig.product.keeper.api.models.UserProfile
import com.livelygig.product.userprofile.api.models.UserProfileResponse

trait UserProfileService extends Service {

  def updateProfile: ServiceCall[UserProfile, UserProfileResponse]

  /**
    * get current logged in user profile
    * @return
    */
  def getUserProfile: ServiceCall[NotUsed, UserProfileResponse]

  /**
    * Accepts the Seq alias uri of the user connection and return
    * their condensed profiles
    * @return
    */
  def getUserConnectionsProfile: ServiceCall[Seq[String], UserProfileResponse]

  def descriptor = {
    import Service._
    named("user").withCalls(
      namedCall("/api/user/updateProfile", updateProfile),
      namedCall("/api/user/getProfile", getUserProfile)
    )
  }
}


