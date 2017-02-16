package client.handler

import diode.{ActionHandler, ActionResult, ModelRW}
import com.livelygig.product.shared.models.ConnectionsModel
import client.rootmodel.ConnectionsRootModel
import com.livelygig.product.shared.dtos._

// Actions
//scalastyle:off
case class UpdateConnections(newConnectionModel: Seq[ConnectionsModel])


class ConnectionHandler[M](modelRW: ModelRW[M, ConnectionsRootModel]) extends ActionHandler(modelRW) {
  override def handle: PartialFunction[Any, ActionResult[M]] = {

    case UpdateConnections(newConnectionsModel) =>
      val cnxnModelMod = if (value.connectionsResponse.nonEmpty){
        value.connectionsResponse ++ newConnectionsModel.filterNot(e=>
          value.connectionsResponse.exists( p=> e.connection.source == p.connection.target || e.connection.target == p.connection.target))

      } else {
        newConnectionsModel
      }
      updated(ConnectionsRootModel(cnxnModelMod))
  }
}