package com.livelygig.product.security

import java.security.Principal
import java.util.UUID
import javax.security.auth.Subject

import com.lightbend.lagom.scaladsl.api.security.ServicePrincipal
import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import play.api.mvc.{AnyContent, Request}

sealed trait UserPrincipal extends Principal {
  val userId: UUID
  override def getName: String = userId.toString
  override def implies(subject: Subject): Boolean = false
}

object UserPrincipal {
  case class ServicelessUserPrincipal(userId: UUID) extends UserPrincipal
  case class UserServicePrincipal(userId: UUID, servicePrincipal: ServicePrincipal) extends UserPrincipal with ServicePrincipal {
    override def serviceName: String = servicePrincipal.serviceName
  }

  def of(userId: UUID, principal: Option[Principal]) = {
    principal match {
      case Some(servicePrincipal: ServicePrincipal) =>
        UserPrincipal.UserServicePrincipal(userId, servicePrincipal)
      case other =>
        UserPrincipal.ServicelessUserPrincipal(userId)
    }
  }
}

object SecurityHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = {
    request.principal match {
      case Some(userPrincipal: UserPrincipal) => request.withHeader("User-Id", userPrincipal.userId.toString)
      case other => request
    }
  }

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader("User-Id") match {
      case Some(userId) =>
        request.withPrincipal(UserPrincipal.of(UUID.fromString(userId), request.principal))
      case None => request
    }
  }

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  lazy val Composed = HeaderFilter.composite(SecurityHeaderFilter, UserAgentHeaderFilter)
}

object ServerSecurity {

  def authenticated[Req, Response](serviceCall: UUID => ServerServiceCall[Req, Response]) =

    ServerServiceCall.compose { requestHeader =>
      val request = SecurityHeaderFilter.transformServerRequest(requestHeader)
      request.principal match {
        case Some(userPrincipal: UserPrincipal) =>
          serviceCall(userPrincipal.userId)
        case other =>
          throw Forbidden("User not authenticated")
      }
    }
}

object ClientSecurity {

  /**
    * Authenticate a client request.
    */
  def authenticate(req: Request[AnyContent]): RequestHeader => RequestHeader = { requestHeader =>
    req.headers.get("userId") match {
      case Some(userId) =>
          val requestWithPrincipal = requestHeader.withPrincipal(UserPrincipal.of(UUID.fromString(userId), requestHeader.principal))
          SecurityHeaderFilter.transformClientRequest(requestWithPrincipal)
      case other =>
          throw Forbidden("User not authenticated")
    }
  }
}



