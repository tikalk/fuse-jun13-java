package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{JsValue, JsObject}
import play.api.libs.iteratee.{Enumerator, Iteratee}

object Application extends Controller {
  
  
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def webSocket = WebSocket.async[JsValue] {request =>  
    GameController.start
  }
  
}