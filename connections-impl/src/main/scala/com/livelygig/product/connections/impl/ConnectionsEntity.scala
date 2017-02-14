package com.livelygig.product.connections.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence._
import com.livelygig.product.connections.api.models.{Connection, ConnectionResponse, ConnectionStatus, NewConnectionResponse}

/**
  * Created by shubham.k on 16-12-2016.
  */
class ConnectionsEntity extends PersistentEntity {

  override type Command = ConnectionCommand

  override type Event = ConnectionEvent

  override type State = ConnectionState

  override def initialState = ConnectionState(Nil)

  override def behavior = {
    case ConnectionState(_) => handleBehaviour
  }

  def handleBehaviour = {
    Actions()
      .onCommand[AddConnection, Done]{
      case (AddConnection(newConnectionUri), ctx, state) =>
        // TODO had to be replaced according to the introduction protocol
        val newConnection = Connection(newConnectionUri,ConnectionStatus.Connected)
        ctx.thenPersist(ConnectionAdded(newConnection))(_ => ctx.reply(Done))
    }.onEvent{
      case (ConnectionAdded(newConnection), state) => {
        val connectionList = state.connectionsAliasUri :+ newConnection
        state.copy(connectionsAliasUri = connectionList)
      }
    }
  }
}
