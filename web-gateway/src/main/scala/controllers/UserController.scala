package controllers

import com.livelygig.product.ResourceClientSecurity
import com.livelygig.product.userprofile.api.UserProfileService
import com.mohiva.play.silhouette.api.Silhouette
import play.api.libs.json.Json
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shubham.k on 15-02-2017.
  */
class UserController(silhouette: Silhouette[DefaultEnv], userProfileService: UserProfileService
                    )(implicit ec: ExecutionContext) extends AbstractController() {

  def getUserProfile = silhouette.SecuredAction.async { implicit request =>
    userProfileService.getUserProfile
      .handleRequestHeader(ResourceClientSecurity.authenticate(request.identity.userUri))
      .invoke()
      .map { res =>
        Ok(Json.toJson(res))
      }
  }

}
