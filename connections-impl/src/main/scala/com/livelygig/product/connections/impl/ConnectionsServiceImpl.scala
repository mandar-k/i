package com.livelygig.product.connections.impl

import com.livelygig.product.connections.api.ConnectionsService
import play.api.Environment

import scala.concurrent.ExecutionContext


/**
  * Created by shubham.k on 25-01-2017.
  */
class ConnectionsServiceImpl(environment: Environment)(implicit ec: ExecutionContext) extends  ConnectionsService {
  override def addConnections() = ???
}
