package com.livelygig.product.alias.impl

import com.livelygig.product.alias.api.AliasService
import play.api.Environment
import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by shubham.k on 25-01-2017.
  */
class AliasServiceImpl(environment: Environment)(implicit ec: ExecutionContext) extends  AliasService {
  override def addAlias() = ???
}
