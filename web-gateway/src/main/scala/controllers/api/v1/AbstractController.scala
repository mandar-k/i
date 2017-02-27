package controllers.api.v1

import org.slf4j.LoggerFactory
import play.api.libs.json.{JsValue, Reads}
import play.api.mvc.{Controller, Request, Result}

import scala.concurrent.Future

/**
 * Created by shubham.k on 08-02-2017.
 */
abstract class AbstractController() extends Controller {
  private val log = LoggerFactory.getLogger(classOf[AbstractController])

  protected def unmarshalJsValue[R](request: Request[JsValue])(block: R => Future[Result])(implicit rds: Reads[R]): Future[Result] =
    request.body.validate[R](rds).fold(
      valid = block,
      invalid = e => {
      val error = e.mkString
      log.error(error)
      Future.successful(BadRequest(error))
    }
    )
}
