package models.services

import com.mohiva.play.silhouette.api.services.IdentityService
import models.UserIdentity

/**
 * Handles actions to users.
 */
trait SilhouetteIdentityService extends IdentityService[UserIdentity] {

}
