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

/*  def loginFromApi(userModel: UserModel): Future[String] = {
    val srpc = new SRPClient(userModel.email, userModel.password)
    val Aval = srpc.getAHex()
    val requestContent1 = upickle.default.write(ApiRequest(ApiTypes.requestTypes.INITIALIZE_SESSION_STEP1_REQUEST,
      InitializeSession(s"agent://email/${userModel.email}?A=$Aval")))
    console.log(s"requestContent1 ${requestContent1}")

    val futureResponse = for {
      requestContent2 <- ajaxPost(requestContent1,"/login").map {
        response =>
          Try(upickle.default.read[ApiResponse[UserLoginResponse]](response)) toOption match {
            case None => throw new ApiError(response)
            case Some(ulr) =>
              val Mval = srpc.getMHex(ulr.content.B, ulr.content.s)
              console.log(s"mval=${Mval}")
              upickle.default.write(ApiRequest(ApiTypes.requestTypes.INITIALIZE_SESSION_STEP2_REQUEST,
                InitializeSession(s"agent://email/${userModel.email}?M=$Mval")))
          }
      }
      result <- ajaxPost(requestContent2,"/login").map {
        response =>
          Try(upickle.default.read[ApiResponse[InitializeSessionResponseCheck]](response)) toOption match {
            case None => throw new ApiError(response)
            case Some(rsp) =>
              if (srpc.matches(rsp.content.M2)) response else throw new Exception("Authentication failed on client")
          }
      }
    } yield result

    futureResponse.recover {
      case ae: ApiError => ae.response
      case e: Throwable => upickle.default.write(ApiRequest(ApiTypes.responseTypes.API_HOST_UNREACHABLE_ERROR, ErrorResponse(e.getMessage)))
    }
  }

  def signupFromApi(signUpModel: SignUpModel): Future[String] = {
    val srpc = new SRPClient(signUpModel.email, signUpModel.password)
    val requestContent1 = upickle.default.write(ApiRequest(ApiTypes.requestTypes.CREATE_USER_STEP1_REQUEST,
      CreateUserStep1(signUpModel.email)))
    val futureResponse = for {
      requestContent2 <- ajaxPost(requestContent1,"/signup").map {
        response =>
          Try(upickle.default.read[ApiResponse[CreateUserStep1Response]](response)) toOption match {
            case None => throw new ApiError(response)
            case Some(rsp) => upickle.default.write(ApiRequest(ApiTypes.requestTypes.CREATE_USER_STEP2_REQUEST,
              CreateUserStep2(signUpModel.email, Map("name" -> signUpModel.name),
                true, rsp.content.salt, srpc.getVerifierHex(rsp.content.salt))))
          }
      }
      result <- ajaxPost(requestContent2,"/signup")
    } yield result

    futureResponse.recover {
      case ae: ApiError => ae.response
      case e: Throwable => upickle.default.write(ApiRequest(ApiTypes.responseTypes.API_HOST_UNREACHABLE_ERROR, ErrorResponse(e.getMessage)))
    }

  }*/


}