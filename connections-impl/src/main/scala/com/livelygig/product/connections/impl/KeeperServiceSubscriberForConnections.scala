package com.livelygig.product.connections.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.livelygig.product.keeper.api
import com.livelygig.product.keeper.api.{KeeperEventsForTopics, KeeperService}
import scala.concurrent.Future

/**
  * Created by shubham.k on 02-02-2017.
  */
class KeeperServiceSubscriberForConnections(keeperService: KeeperService) {
  keeperService.keeperTopicProducer.subscribe.atLeastOnce(Flow[KeeperEventsForTopics].mapAsync(1) {
    case uc:api.UserCreated =>
      Future.successful(Done)
    case _ =>
      Future.successful(Done)
  })
}
