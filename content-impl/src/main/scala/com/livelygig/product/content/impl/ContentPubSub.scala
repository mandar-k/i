package com.livelygig.product.content.impl

import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.pubsub.{PubSubComponents, TopicId}
import com.livelygig.product.content.api.models.{UserContent}

/**
 * Created by shubham.k on 29-12-2016.
 */
class ContentPubSub(actSys: ActorSystem) extends PubSubComponents {
  private final val MAX_TOPICS = 1024
  override def actorSystem = actSys

  def refFor(userId: String) = {
    pubSubRegistry.refFor(TopicId[UserContent](topicQualifier(userId)))
  }

  def topicQualifier(userId: String) = {
    //    val MAX_TOPICS = 1024
    String.valueOf(Math.abs(userId.hashCode()) % MAX_TOPICS)
  }
}
