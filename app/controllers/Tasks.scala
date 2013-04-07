/**
 * Created with IntelliJ IDEA.
 * User: steve
 * Date: 4/7/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
package controllers

import play.api.mvc._
import models.Task
import play.api.libs.json.Json

object Tasks extends Controller {
  def list = Action { implicit request =>
    val tasks = Task.all()

    render {
      case Accepts.Json() => Ok(Json.toJson(tasks))
    }
  }
}

