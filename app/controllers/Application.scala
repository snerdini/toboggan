package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._

object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Redirect(routes.Application.tasks())
  }

  def tasks = Action { implicit request =>
    Ok(views.html.index(taskForm))
  }

  def foo = Action {
    Async {
      WS.url("http://www.playframework.com").get().map { response =>
        Ok(response.body).as(HTML)
      }
    }
  }
}