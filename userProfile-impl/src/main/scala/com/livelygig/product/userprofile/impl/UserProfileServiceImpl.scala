package com.livelygig.product.userprofile.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.userprofile.api.UserProfileService

import scala.concurrent.ExecutionContext


class UserProfileServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends UserProfileService {

  override def updateProfile = ???

  private def refFor(email:String) = registry.refFor[UserProfileEntity](email)
}
