package com.livelygig.product.user.api

import java.util.UUID

import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 21-12-2016.
  */
case class User(id: UUID, email:String, password: String, name:String)

object User {
  implicit val format: Format[User] = Json.format

}