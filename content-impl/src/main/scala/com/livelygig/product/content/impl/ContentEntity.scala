package com.livelygig.product.content.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.content.api.models.UserContent

/**
  * Created by shubham.k on 16-12-2016.
  */
class ContentEntity(msgPubSub: ContentPubSub)  extends PersistentEntity {

  override type Command = ContentCommand

  override type Event = ContentEvent

  override type State = Option[UserContent]

  override def initialState = None

  override def behavior = {
    case None => notCreated
    case Some(_) => created
  }

  private val notCreated = {
    Actions()
      .onCommand[AddContent, Done]{
      case (AddContent(msg),ctx,state) => {
        ctx.thenPersist(ContentPosted(msg))(evt => {
          ctx.reply(Done)
          msgPubSub.refFor("MESSAGE").publish(msg)
        })
      }
    }
      .onEvent{
        case (ContentPosted(msg), state) => Some(msg)}
  }

  private val created = {
    Actions()
      .onReadOnlyCommand[AddContent, Done] {
      case (AddContent(msg),ctx, _) => {
        ctx.invalidCommand("Error in posting the message.")
      }
    }
      .onReadOnlyCommand[GetContent.type , Option[UserContent]] {
      case (GetContent, ctx, state) => {
        ctx.reply(state)
      }
    }
  }
}