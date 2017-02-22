package com.livelygig.product

import java.security.Principal
import java.util.UUID
import javax.security.auth.Subject

import com.lightbend.lagom.scaladsl.api.security.ServicePrincipal
import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

sealed trait ResourcePrincipal extends Principal {
  val serviceId: UUID
  override def getName: String = serviceId.toString
  override def implies(subject: Subject): Boolean = false
}

object ResourcePrincipal {
  case class ServicelessResourcePrincipal(serviceId: UUID) extends ResourcePrincipal
  case class ResourceServicePrincipal(serviceId: UUID, servicePrincipal: ServicePrincipal) extends ResourcePrincipal with ServicePrincipal {
    override def serviceName: String = servicePrincipal.serviceName
  }

  def of(authKey: UUID, principal: Option[Principal]) = {
    principal match {
      case Some(servicePrincipal: ServicePrincipal) =>
        ResourcePrincipal.ResourceServicePrincipal(authKey, servicePrincipal)
      case other =>
        ResourcePrincipal.ServicelessResourcePrincipal(authKey)
    }
  }
}

object SecurityHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = {
    request.principal match {
      case Some(resourcePrincipal: ResourcePrincipal) => request.withHeader("ServiceId", resourcePrincipal.serviceId.toString)
      case other => request
    }
  }

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader("ServiceId") match {
      case Some(serviceId) =>
        request.withPrincipal(ResourcePrincipal.of(UUID.fromString(serviceId), request.principal))
      case None => request
    }
  }

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  lazy val Composed = HeaderFilter.composite(SecurityHeaderFilter, UserAgentHeaderFilter)
}

object KeeperServerSecurity {

  def authenticated[Request, Response](serviceCall: UUID => ServerServiceCall[Request, Response]) =

    ServerServiceCall.compose { requestHeader =>
      val request = SecurityHeaderFilter.transformServerRequest(requestHeader)
      request.principal match {
        case Some(resourcePrincipal: ResourcePrincipal) =>
          serviceCall(resourcePrincipal.serviceId)
        case other =>
          throw Forbidden("Service not authenticated")
      }
    }
}

object KeeperClientSecurity {

  /**
   * Authenticate a resource client request.
   */
  def authenticate(serviceId: UUID): RequestHeader => RequestHeader = { request =>
    request.withPrincipal(ResourcePrincipal.of(serviceId, request.principal))
  }
}

