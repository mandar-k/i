package com.livelygig.product.userprofile.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.livelygig.product.keeper.api.models.UserProfile
import com.livelygig.product.userprofile.api.models.UserProfileResponse

trait UserProfileService extends Service {

  def updateProfile: ServiceCall[UserProfile, UserProfileResponse]

  def descriptor = {
    import Service._
    named("user").withCalls(
      namedCall("/api/user/updateProfile", updateProfile)
    )
  }
}


