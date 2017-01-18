package com.livelygig.product.keeper.impl.models

import java.util.{Date, UUID}

import play.api.libs.json.{Format, Json}

/**
  * Created by shubham.k on 16-01-2017.
  */
case class UserLoginInfo(time: Date, userEmail: String, authKeyGenerated: String, userId: UUID)


object UserLoginInfo{
  implicit val format:Format[UserLoginInfo] = Json.format
}
