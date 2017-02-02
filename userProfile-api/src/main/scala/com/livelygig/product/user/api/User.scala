package com.livelygig.product.user.api

import java.util.UUID

import be.objectify.deadbolt.scala.models.{Permission, Role}
import be.objectify.deadbolt.scala.models.Subject
import play.api.libs.json.{Format, Json}
/**
  * Created by shubham.k on 21-12-2016.
  */

case class SecurityRole(name: String) extends Role
case class UserPermission(value: String) extends Permission

case class User(id: UUID, email:String, password: String, name:String) extends Subject{
  override def roles: List[SecurityRole] =
    List(SecurityRole("user"),
      SecurityRole("admin"))

  override def permissions: List[UserPermission] =
    List(UserPermission("messages.add"))

  override def identifier: String = name

}

object User {
  implicit val format: Format[User] = Json.format
}