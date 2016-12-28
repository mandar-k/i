package com.livelygig.product.message.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.message.api.Message

/**
  * Created by shubham.k on 16-12-2016.
  */
class MessageTimelineEntity extends PersistentEntity{

  private final val MAX_TOPICS = 1024
  override type Command = MessageCommand

  override type Event = MessageEvent

  override type State = Option[Message]

  override def initialState = None

  override def behavior = {
    case None => notCreated
    case Some(message) => created
  }

  private val notCreated = {
    Actions()
      .onCommand[AddMessage, Done]{
      case (AddMessage(msg),ctx,state) => {
        ctx.thenPersist(MessagePosted(msg), evt => {
          ctx.reply(Done)
//          refFor(msg.userId.toString).publish(msg)
        })
      }
    }
      .onEvent{
        case (MessagePosted(msg), state) => state}
  }

  private val created = {
    Actions()
      .onReadOnlyCommand[AddMessage, Done] {
      case (AddMessage(msg),ctx, _) => {
        ctx.invalidCommand("Error in posting the message.")
      }
    }
  }

  def refFor(userId: String) = {
//    pubsub.refFor(TopicId[Message](topicQualifier(userId)))
  }

  def topicQualifier (userId: String) = {
    String.valueOf(Math.abs(userId.hashCode()) % MAX_TOPICS)
  }
}