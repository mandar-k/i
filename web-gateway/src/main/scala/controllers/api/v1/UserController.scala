package controllers.api.v1

import play.api.mvc.Action

/**
 * Created by shubham.k on 27-02-2017.
 */
class UserController extends AbstractController {
  def getProfile = Action(parse.anyContent) {
    implicit request =>
      Ok("")
  }
  def getConnections = Action(parse.anyContent) {
    implicit request =>
      Ok("")
  }
  def getCnxnProfiles = Action(parse.anyContent) {
    implicit request =>
      Ok("")
  }
}
