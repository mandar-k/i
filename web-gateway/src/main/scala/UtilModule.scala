import com.softwaremill.macwire._
import play.api.http.HttpErrorHandler
import play.api.routing.Router
import play.api.{Configuration, Environment}
import play.core.SourceMapper

trait UtilModule {
  def environment: Environment
  def configuration: Configuration
  def sourceMapper: Option[SourceMapper]
  def routerOption: Option[Router]

  lazy val errorHandler: HttpErrorHandler = wire[WebGatewayErrorHandler]
}
