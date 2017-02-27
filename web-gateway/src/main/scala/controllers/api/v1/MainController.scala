package controllers.api.v1

import play.api.mvc.Action

/**
 * Created by shubham.k on 27-02-2017.
 */
class MainController extends AbstractController {
  def connect = Action(parse.anyContent) {
    implicit request =>
      Ok("")
  }

  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }
}
