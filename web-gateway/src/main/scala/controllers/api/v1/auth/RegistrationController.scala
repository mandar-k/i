package controllers.api.v1.auth

import java.util.UUID

import com.livelygig.product.shared.{CreateUser, InitializeSession}
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.api.v1.AbstractController
import play.api.mvc.Action
import silhouetteservices.SilhouetteIdentityService

import scala.concurrent.Future

/**
 * Created by shubham.k on 27-02-2017.
 */
class RegistrationController(silhouetteIdentityService: SilhouetteIdentityService) extends AbstractController {
  def signup = Action.async(parse.json) { implicit request =>
    unmarshalJsValue[CreateUser](request) {
      data =>
        {
          val loginInfo = LoginInfo(CredentialsProvider.ID, data.email.toLowerCase)
          silhouetteIdentityService.retrieve(loginInfo).flatMap {
            case Some(user) => Future.successful(
              Redirect(controllers.routes.ViewController.signup()).flashing("error" -> "That email address is already in use.")
            )
            case None =>
              val authInfo = hasher.hash(data.password)
              val role = Role.withName(SettingsService(SettingKey.DefaultNewUserRole))
              val user = User(
                id = UUID.randomUUID,
                username = data.username,
                preferences = UserPreferences.empty,
                profile = loginInfo,
                role = role
              )
              val userSavedFuture = userService.save(user)
              val result = request.session.get("returnUrl") match {
                case Some(url) => Redirect(url).withSession(request.session - "returnUrl")
                case None => Redirect(controllers.routes.HomeController.home())
              }
              for {
                authInfo <- authInfoRepository.add(loginInfo, authInfo)
                authenticator <- app.silhouette.env.authenticatorService.create(loginInfo)
                value <- app.silhouette.env.authenticatorService.init(authenticator)
                result <- app.silhouette.env.authenticatorService.embed(value, result)
                userSaved <- userSavedFuture
              } yield {
                app.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
                app.silhouette.env.eventBus.publish(LoginEvent(userSaved, request))
                result.flashing("success" -> "You're all set!")
              }
          }
        }
    }
  }
}
