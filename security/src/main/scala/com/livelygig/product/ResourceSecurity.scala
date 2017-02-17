package com.livelygig.product

import java.security.Principal
import javax.security.auth.Subject

import com.lightbend.lagom.scaladsl.api.security._
import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server._

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
      case Some(userPrincipal: UserPrincipal) => request.withHeader("userUri", userPrincipal.userUri)
      case other => request
    }
  }

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader("userUri") match {
      case Some(userUri) =>
        request.withPrincipal(UserPrincipal.of(userUri, request.principal))
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
          serviceCall(userPrincipal.userUri, requestHeader)
        case other =>
          throw Forbidden("User not authenticated")
      }
    }
}

object ResourceClientSecurity {

  /**
    * Authenticate a resource client request.
    */
  def authenticate(userUri: String): RequestHeader => RequestHeader = { requestHeader =>
    val requestWithPrincipal = requestHeader.withPrincipal(UserPrincipal.of(userUri, requestHeader.principal))
    SecurityHeaderFilter.transformClientRequest(requestWithPrincipal)
  }
}