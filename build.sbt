lazy val sharedJs = Shared.sharedJs

lazy val client = Client.client

lazy val sharedJvm = Shared.sharedJvm

lazy val webGateway = Server.webGateway

lazy val keeperApi = KeeperApi.keeperApi

lazy val userProfileApi = UserProfileApi.userProfileApi

lazy val contentApi = ContentApi.contentApi

lazy val connectionsApi = ConnectionsApi.connectionsApi

lazy val emailNotificationsApi = EmailNotificationsApi.emailNotificationsApi

lazy val keeperImpl = KeeperImpl.keeperImpl

lazy val userProfileImpl = UserProfileImpl.userProfileImpl

lazy val contentImpl = ContentImpl.contentImpl

lazy val connectionsImpl = ConnectionsImpl.connectionsImpl

lazy val emailNotificationsImpl = EmailNotificationsImpl.emailNotificationsImpl

lazy val security = ServiceSecurity.security

lagomCassandraCleanOnStart in ThisBuild := true