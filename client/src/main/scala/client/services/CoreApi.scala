package client.services


/**
  * Created by bhagyashree.b on 24-12-2016.
  */

import client.dtos.UserModel
import client.dtos.SignUpModel
import client.facades.SRPClient
import shared.dtos._
import org.scalajs.dom._
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.{Date, JSON}
import scala.util.{Failure, Success, Try}
import scala.language.postfixOps
import org.scalajs.dom.ext.Ajax
import shared.models.{EmailValidationModel}
import client.sessionitems.SessionItems
import client.modules.ConnectionList
import client.utils.{ConnectionsUtils, LabelsUtils}



object CoreApi {
  // scalastyle:ignore
  var CREATE_USER_REQUEST = "createUserRequest"

  def ajaxPost(requestContent: String, apiUrl : String): Future[String] = {
       console.log("SignUpModel "+upickle.default.write(requestContent))
    Ajax.post(
      url = apiUrl,
      data =requestContent,
      headers = Map("Content-Type" -> "application/json;charset=UTF-8")
    ).map(_.responseText)
  }

  def signupFromApi(signUpModel: SignUpModel): Future[String] = {
    ajaxPost(upickle.default.write(signUpModel),"/signup")
  }

  def loginFromApi(userModel: UserModel): Future[String] = {
    ajaxPost(upickle.default.write(userModel),"/login")
  }

  def validateToken(authToken: String): Future[String] = {
    Ajax.get(
      url = "validateToken",
      headers = Map("X-Auth-Token" -> authToken)
    ).map(_.responseText)
  }


}