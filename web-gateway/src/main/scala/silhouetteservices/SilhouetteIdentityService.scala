package silhouetteservices

import com.livelygig.product.keeper.api.KeeperService
import com.livelygig.product.keeper.api.models.{UserAuthRes, UserFound}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.UserIdentity

import scala.concurrent.{ExecutionContext, Future}

/**
 * Handles actions to users.
 *
 */
class SilhouetteIdentityService(keeperService: KeeperService)(implicit val executionContext: ExecutionContext)
    extends IdentityService[UserIdentity] {
  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve a user.
   * @return The retrieved user or None if no user could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]] = {
    keeperService.getUriFromEmail.invoke(loginInfo.providerKey).map {
      case UserAuthRes(_, UserFound(uri, username, email)) => Some(UserIdentity(uri, loginInfo, username))
      case _ => None
    }

  }

}
