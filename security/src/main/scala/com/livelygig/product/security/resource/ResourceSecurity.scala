package com.livelygig.product.security.resource

import java.security.Principal
import javax.security.auth.Subject

import com.lightbend.lagom.scaladsl.api.security.ServicePrincipal
import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

sealed trait UserPrincipal extends Principal {
  val authKey: String
  override def getName: String = authKey
  override def implies(subject: Subject): Boolean = false
}

object UserPrincipal {
  case class ServicelessUserPrincipal(authKey: String) extends UserPrincipal
  case class UserServicePrincipal(authKey: String, servicePrincipal: ServicePrincipal) extends UserPrincipal with ServicePrincipal {
    override def serviceName: String = servicePrincipal.serviceName
  }

  def of(authKey: String, principal: Option[Principal]) = {
    principal match {
      case Some(servicePrincipal: ServicePrincipal) =>
        UserPrincipal.UserServicePrincipal(authKey, servicePrincipal)
      case other =>
        UserPrincipal.ServicelessUserPrincipal(authKey)
    }
  }
}

object SecurityHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = {
    request.principal match {
      case Some(userPrincipal: UserPrincipal) => request.withHeader("X-Auth-Token", userPrincipal.authKey)
      case other => request
    }
  }

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader("X-Auth-Token") match {
      case Some(token) =>
        request.withPrincipal(UserPrincipal.of(token, request.principal))
      case None => request
    }
  }

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  lazy val Composed = HeaderFilter.composite(SecurityHeaderFilter, UserAgentHeaderFilter)
}

object ResourceServerSecurity {

  def authenticated[Req, Response](serviceCall: (String, RequestHeader) => ServerServiceCall[Req, Response]) =

    ServerServiceCall.compose { requestHeader =>
      val request = SecurityHeaderFilter.transformServerRequest(requestHeader)
      request.principal match {
        case Some(userPrincipal: UserPrincipal) =>
          serviceCall(userPrincipal.authKey, requestHeader)
        case other =>
          throw Forbidden("User not authenticated")
      }
    }
}

object ResourceClientSecurity {

  /**
    * Authenticate a resource client request.
    */
  def authenticate(): RequestHeader => RequestHeader = { requestHeader =>

    requestHeader.getHeader("X-Auth-Token") match {
      case Some(token) =>
          val requestWithPrincipal = requestHeader.withPrincipal(UserPrincipal.of(token, requestHeader.principal))
          SecurityHeaderFilter.transformClientRequest(requestWithPrincipal)
      case other =>
          throw Forbidden("User not authenticated")
    }
  }
}

object AuthClientSecurity {
  /**
    * Authenticate a request from the resource service
    */
}



