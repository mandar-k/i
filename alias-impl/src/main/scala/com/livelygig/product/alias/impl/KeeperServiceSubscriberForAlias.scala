package com.livelygig.product.alias.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.livelygig.product.keeper.api
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import scala.concurrent.Future

/**
  * Created by shubham.k on 02-02-2017.
  */
class KeeperServiceSubscriberForAlias(keeperService: KeeperService) {
  keeperService.keeperTopicProducer.subscribe.atLeastOnce(Flow[KeeperEventsForTopics].mapAsync(1) {
    case uc:api.UserCreated =>
      // TODO create default alias
      Future.successful(Done)
    case _ =>
      Future.successful(Done)
  })
}
