package controllers

import com.google.inject.Inject
import com.livelygig.product.user.api.{User, UserService}
import play.api.mvc._
import play.api.Environment

import scala.concurrent.{ExecutionContext, Future}

class Main(userService: UserService) (implicit env: Environment, ec: ExecutionContext) extends Controller {

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

  def login = Action.async{implicit rh =>
    println(rh.body.asJson)
    userService.login.invoke(User("yo","")).map{
      msg => Ok(msg)
    }
  }

}