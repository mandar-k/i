package client.rootmodel

import com.livelygig.product.shared.dtos.Connection
import com.livelygig.product.shared.models.ConnectionsModel

// scalastyle:off
case class ConnectionsRootModel(connectionsResponse: Seq[ConnectionsModel]) {
  def updated(newConnectionResponse: ConnectionsModel) = {
    connectionsResponse.indexWhere(_.connection.target == newConnectionResponse.connection.target) match {
      case -1 =>
        ConnectionsRootModel(connectionsResponse :+ newConnectionResponse)
      case target =>
        ConnectionsRootModel(connectionsResponse.updated(target, newConnectionResponse))
    }
  }
}
