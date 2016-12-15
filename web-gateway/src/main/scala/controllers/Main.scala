package controllers

import com.google.inject.Inject
import com.livelygig.product.user.api.UserService
import play.api.mvc._
import play.api.Environment

import scala.concurrent.ExecutionContext

class Main(userService: UserService) (implicit env: Environment, ec: ExecutionContext) extends Controller {
  userService.sayHello.invoke().map(s =>println)
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