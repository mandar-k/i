package controllers

import java.util.UUID

import com.livelygig.product.user.api.{User, UserService}
import play.api.mvc._
import play.api.Environment
import play.api.libs.json.Json

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


  def signup = Action.async { implicit rh =>
    println("Signup" + rh.body.asJson)
    rh.body.asJson match {
      case Some(data) => {
//        data.as[Message]
        var email = (data \ ("email")).as[String]
        var password = (data \ ("password")).as[String]
        var name = (data \ ("name")).as[String]
        userService.signup.invoke(User(UUID.randomUUID(), email, password, name)).map {
          msg => Ok("")
        }
      }
      case None => {
        userService.signup.invoke(User(UUID.randomUUID(), "", "testq2", "testq2")).map {
          msg => Ok("")
        }
      }
    }
  }

  def login = Action.async { implicit rh =>
    println("Signup" + rh.body.asJson)
    rh.body.asJson match {
      case Some(data) => {
        var email = (data \ ("email")).as[String]
        var password = (data \ ("password")).as[String]
//        var name = (data \ ("name")).as[String]
        userService.login.invoke(User(UUID.randomUUID(), email, password, "name")).map {
          msg => Ok(views.html.index("LivelyGig"))
        }
      }
      case None => {
        userService.login.invoke(User(UUID.randomUUID(), "", "testq2", "testq2")).map {
          msg => Ok("")
        }
      }
    }
  }
}