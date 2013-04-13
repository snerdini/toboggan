package controllers

import play.api.mvc._
import models.Task
import play.api.libs.json._

/**
 * Controller that groups together all actions
 * related to managing tasks.
 */
object Tasks extends Controller {

  /**
   * Action called to get a list of active
   * tasks in the system.
   *
   * @return JSON object representing tasks
   */
  def list(pageSize: Long = 5, pageIndex: Long = 0) = Action { implicit request =>
    val tasks = Task.load(pageSize, pageIndex)
    val taskCount = Task.count

    render {
      case Accepts.Json() => Ok(
        Json.obj(
          "totalRows" -> taskCount,
          "tasks" -> Json.toJson(tasks))
        )
    }
  }

  /**
   * Action that is called client-side to create a new
   * task for the end user and stuff.
   *
   * @return JSON object representing the newly created task
   */
  def newTask = Action(parse.json) { request =>
    val label = (request.body \ "label").as[String]
    val newId = Task.create(label)
    Ok(Json.obj("id" -> newId, "label" -> label))
  }

  /**
   * Action called asynchronously to delete a task.
   *
   * @param id  the ID of the task to delete
   * @return response message for the UI layer
   */
  def deleteTask(id: Long) = Action { implicit request =>
    Task.delete(id)
    Ok(Json.obj("success" -> true, "message" -> "Task deleted successfully."))
  }
}

