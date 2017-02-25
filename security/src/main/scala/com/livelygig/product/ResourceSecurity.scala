package com.livelygig.product

import java.security.Principal
import javax.security.auth.Subject

import com.lightbend.lagom.scaladsl.api.security.ServicePrincipal
import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

sealed trait UserPrincipal extends Principal {
  val userUri: String
  override def getName: String = userUri
  override def implies(subject: Subject): Boolean = false
}

object UserPrincipal {
  case class ServicelessUserPrincipal(userUri: String) extends UserPrincipal
  case class UserServicePrincipal(userUri: String, servicePrincipal: ServicePrincipal) extends UserPrincipal with ServicePrincipal {
    override def serviceName: String = servicePrincipal.serviceName
  }

  def of(userUri: String, principal: Option[Principal]) = {
    principal match {
      case Some(servicePrincipal: ServicePrincipal) =>
        UserPrincipal.UserServicePrincipal(userUri, servicePrincipal)
      case other =>
        UserPrincipal.ServicelessUserPrincipal(userUri)
    }
  }
}

object SecurityHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = {
    request.principal match {
      case Some(userPrincipal: UserPrincipal) => request.withHeader("User-Id", userPrincipal.userUri)
      case other => request
    }
  }

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader("User-Id") match {
      case Some(userUri) =>
        request.withPrincipal(UserPrincipal.of(userUri, request.principal))
      case None => request
    }
  }

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  lazy val Composed = HeaderFilter.composite(SecurityHeaderFilter, UserAgentHeaderFilter)
}

object ServerSecurity {

  def authenticated[Request, Response](serviceCall: String => ServerServiceCall[Request, Response]) =
    ServerServiceCall.compose { requestHeader =>
      requestHeader.principal match {
        case Some(userPrincipal: UserPrincipal) =>
          serviceCall(userPrincipal.userUri)
        case other =>
          throw Forbidden("User not authenticated")
      }
    }

}

object ClientSecurity {

  /**
   * Authenticate a client request.
   */
  def authenticate(userUri: String): RequestHeader => RequestHeader = { request =>
    request.withPrincipal(UserPrincipal.of(userUri, request.principal))
  }
}

