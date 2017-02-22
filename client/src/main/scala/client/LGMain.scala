package client

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js
@JSExport("LGMain")
object LGMain extends js.JSApp {

  @JSExport
  def main(): Unit = {
    println("client started")
    println("X-AUTH-TOKEN")
    println("CSRF Token")
    //    navigate()
  }

  /*
  def navigate() = {
    getToken match {
      case Some(token) => {
        CoreApi.validateToken(token).map{
          _ => {
            window.localStorage.setItem("X-Auth-Token", token)
            window.location.replace("/app#messages")
          }
        }.recover{case _=>
          window.location.href = "/signIn"
      }
      }
      case None => window.location.href = "/signIn"
    }
  }

  def getToken : Option[String] = {
    val tokenFromLocalStorage = window.localStorage.getItem("X-Auth-Token")
    if (tokenFromLocalStorage != null)
      Some(tokenFromLocalStorage)
    else {
      getParameterByName("x_auth_token")
    }

  }

  def  getParameterByName(name: String)  = {
    val url = window.location.href
    val pattern="""(http|htpps)([A-Za-z0-9\:\/\%\-\.]*)\?""".r
    val temp_url=pattern.replaceFirstIn(url,"")
    val split = temp_url.split("&|=").grouped(2)/*.map(js.URIUtils.decodeURIComponent)*/
    val queryStringMap = temp_url.split("&").map(_.split("=")).map(arr => arr(0) -> arr(1)).toMap
    queryStringMap.get(name)
  }*/
}
