package controllers

import com.google.inject.Inject
import play.api.mvc._
import play.api.{Environment}
import services.ApiService

class Main @Inject()(implicit env: Environment) extends Controller {
  val apiService = new ApiService()

  def index = Action {
    Ok(views.html.index("LivelyGig"))
  }

  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"Application - CLIENT - $msg")
      }
      Ok("")
  }
}