package com.livelygig.product.userprofile.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import com.livelygig.product.keeper.api
import com.livelygig.product.userprofile.api.models.{UserAlias, UserProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 10-02-2017.
  */
class KeeperSubscriberForUserProfiles(keeperService: KeeperService) {

}


class KeeperServiceSubscriberForEmailNotification(keeperService: KeeperService, registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) {
  keeperService.keeperTopicProducer.subscribe.atLeastOnce(Flow[KeeperEventsForTopics].mapAsync(1) {
    case uc:api.UserCreated =>
      val userProfile = UserProfile(None,None, Seq(UserAlias(uc.userName,true)))
      registry.refFor[UserProfileEntity](uc.userUri).ask(CreateProfile(userProfile))
    case uc:api.UserActivated =>
      registry.refFor[UserProfileEntity](uc.userUri).ask(ActivateUserProfile)
    case _ =>
      Future.successful(Done)
  })
}