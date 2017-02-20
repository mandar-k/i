package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.livelygig.product.ResourceServerSecurity
import com.livelygig.product.userprofile.api.UserProfileService
import com.livelygig.product.userprofile.api.models.{ProfileListResponse, UserConnectionProfile, UserProfile, UserProfileResponse}
import scala.concurrent.{ExecutionContext, Future}


class UserProfileServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends UserProfileService {

  override def updateProfile = ???

  override def getUserProfile = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { _ =>
    refFor(userUri).ask(GetProfile)
  })

  /**
    * Get the profile from the entity
    * @return
    */
  override def getUserConnectionsProfile = ResourceServerSecurity.authenticated((userUri, rh) => ServerServiceCall { aliasUriList =>
    val profileFutures = aliasUriList.map{aliasUri =>
      refFor(aliasUri).ask(GetProfile).map{
        e =>
          val userProfile = e.content.asInstanceOf[UserProfile]
          UserConnectionProfile(aliasUri , userProfile.name, userProfile.avatar)
      }
    }
    val futureOfProfiles = Future.sequence(profileFutures)
    futureOfProfiles.map(e => UserProfileResponse("connectionsProfile", ProfileListResponse(e)))
  })

  private def refFor(email:String) = registry.refFor[UserProfileEntity](email)
}
