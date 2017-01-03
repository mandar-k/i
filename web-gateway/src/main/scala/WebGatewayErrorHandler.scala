import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent.Future
import org.slf4j.LoggerFactory

/**
  * Created by shubham.k on 03-01-2017.
  */
class WebGatewayErrorHandler @Inject()(
                                        env: Environment,
                                        config: Configuration,
                                        sourceMapper: OptionalSourceMapper,
                                        router: Provider[Router]
                                      ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  private val log = LoggerFactory.getLogger(classOf[WebGatewayErrorHandler])


  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      Status(statusCode)("A client error occurred: " + message)
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable) = {
    log.error("Server error " + exception.getMessage + "for uri" + request.uri)
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}
