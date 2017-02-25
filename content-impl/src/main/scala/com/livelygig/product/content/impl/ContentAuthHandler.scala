package com.livelygig.product.content.impl

import java.util.UUID

import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{AuthorizationInfo, UserPermission, UserRole}
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, RequestHeader}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by shubham.k on 1/17/2017.
 */

class Subject(authInfo: AuthorizationInfo) {
  def identifier: String = ""

  def permissions: Seq[UserPermission] = authInfo.permissions

  def roles: Seq[UserRole] = authInfo.roles
}

class ConstraintAnalyser {

  type RoleGroup = Array[UserRole]
  type RoleGroups = List[RoleGroup]
  type Permission = UserPermission

  def hasAllRoles(
    subject: Subject,
    requiredRoleNames: RoleGroup
  ): Boolean = {
    def roleMatch(
      requiredRoleName: UserRole,
      subjectRoles: UserRole
    ) = {
      requiredRoleName.equals(subjectRoles)
    }
    subject.roles
      .map(subjectRoles => for (requiredRole <- requiredRoleNames) yield roleMatch(requiredRole, subjectRoles))
      .exists(wasMatched => wasMatched.foldLeft(if (requiredRoleNames.isEmpty) false else true)(_ && _))
  }

  def hasRolesAndPermissions(
    roleGroups: RoleGroups,
    permissions: Permission,
    handler: ContentAuthHandler,
    rh: RequestHeader
  ): Future[Boolean] = {
    def checkRoles(subject: Subject, current: RoleGroup, remaining: RoleGroups): Boolean = {
      if (hasAllRoles(subject, current)) true
      else if (remaining.isEmpty) false
      else checkRoles(subject, remaining.head, remaining.tail)
    }

    def checkPermissions(subject: Subject, permission: Permission): Boolean = {
      subject.permissions.map { subjectPermission => permission.equals(subjectPermission)
      }.foldLeft(false)(_ || _)
    }

    if (roleGroups.isEmpty) throw Forbidden("Authorization failed")
    else {
      handler.getSubject(rh).map { subject =>
        if (checkRoles(subject, roleGroups.head, roleGroups.tail) && checkPermissions(subject, permissions)) true
        else false
      }
    }
  }
}

class ContentAuthHandler(keeperService: KeeperService) {
  def getSubject[A](requestHeader: RequestHeader): Future[Subject] = {
    keeperService.authorize
      //      .handleRequestHeader(KeeperClientSecurity.authenticate(UUID.randomUUID()))
      .invoke("")
      .map { e =>
        new Subject(e)
      }
  }
}
