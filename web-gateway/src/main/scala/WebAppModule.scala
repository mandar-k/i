import com.mohiva.play.silhouette.api.{Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.api.actions._
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, AvatarService}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, PasswordHasher, PlayHTTPLayer}
import com.mohiva.play.silhouette.impl.providers.{CredentialsProvider, SocialProviderRegistry}
import models.daos.{AuthTokenDAOImpl, UserDAOImpl}
import models.services.{AuthTokenServiceImpl, UserService, UserServiceImpl}
import play.api.i18n.{Langs, MessagesApi}
import utils.auth.{CustomSecuredErrorHandler, CustomUnsecuredErrorHandler, DefaultEnv}
import play.api.Configuration
import com.softwaremill.macwire._


trait WebAppModule {

  def messagesApi: MessagesApi

  def socialProviderRegistry: SocialProviderRegistry

  def userService: UserService
  def authInfoRepository: AuthInfoRepository
  def credentialsProvider: CredentialsProvider
  def configuration: Configuration
  def clock: Clock
  def avatarService: AvatarService
  def passwordHasher: PasswordHasher

  lazy val authToken = new AuthTokenDAOImpl
  lazy val authTokenService = new AuthTokenServiceImpl(authToken, clock)
  lazy val securedErrorHandler: SecuredErrorHandler = wire[CustomSecuredErrorHandler]
  lazy val unSecuredErrorHandler: UnsecuredErrorHandler = wire[CustomUnsecuredErrorHandler]
  lazy val userAwareAction = new DefaultUserAwareAction(new DefaultUserAwareRequestHandler)
  lazy val securedAction: SecuredAction = new DefaultSecuredAction(new DefaultSecuredRequestHandler(securedErrorHandler))
  lazy val unsecuredAction: UnsecuredAction = new DefaultUnsecuredAction(new DefaultUnsecuredRequestHandler(unSecuredErrorHandler))

}
