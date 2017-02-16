package com.livelygig.product.shared

/**
  * Created by shubham.k on 16-02-2017.
  */
case class UserProfile(avatar: Option[String], firstname: Option[String], lastname: Option[String],
                       address: Option[String], listOfAlias: Seq[String], defaultAlias: String)
case class ConnectionProfile(avatar: Option[String], firstname: Option[String], lastname: Option[String])
