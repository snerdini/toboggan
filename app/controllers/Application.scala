package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Redirect(routes.Application.tasks())
  }

  def tasks = Action { implicit request =>
    val tasks = Task.all()

    Ok(views.html.index(tasks, taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    ).flashing("wtf" -> "Added.")
  }

  def deleteTask(id: Long) = Action { implicit request =>
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def foo = Action {
    Async {
      WS.url("http://www.playframework.com").get().map { response =>
        Ok(response.body).as(HTML)
      }
    }
  }
}