package models

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._
import anorm.~

case class Task(id: Long, label: String)

object Task {

  /**
   * Enable manual conversion to JSON object for async client requests
   */
  implicit object TaskFormat extends Format[Task] {
    def writes(task: Task):JsValue  = JsObject(List(
      "id" -> JsNumber(task.id),
      "label" -> JsString(task.label)
    ))

    def reads(json: JsValue): JsResult[Task] = JsSuccess {
      Task(
        (json \ "id").as[Long],
        (json \ "label").as[String]
      )
    }
  }

  val task = {
    get[Long]("id") ~
    get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

  /**
   * Gets a list of all the tasks in the datastore.
   *
   * @return list of all tasks
   */
  def all : List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task")
      .as(task *)
  }

  /**
   * Retrieves a page of tasks to list.
   *
   * @param pageSize  the count of items that should appear on the page
   * @param pageIndex the index of the page that should be loaded
   * @return list of tasks from the datastore
   */
  def load(pageSize: Long = 5, pageIndex: Long = 0): List[Task] = DB.withConnection { implicit c =>
    val offset = pageSize * pageIndex
    SQL("select * from task limit {pageSize} offset {offset}")
      .on('pageSize -> pageSize, 'offset -> offset)
      .as(task *)
  }

  /**
   * Gets a count of all the tasks in the datastore.
   *
   * @return count of all tasks
   */
  def count = DB.withConnection { implicit c =>
    SQL("select count(*) as c from task").as(scalar[Long].single)
  }

  /**
   * Creates a new task.
   *
   * @param label the label of the new task
   * @return void
   */
  def create(label: String) = DB.withConnection { implicit c =>
    SQL("insert into task (label) values ({label})").on(
      'label -> label
    ).executeUpdate()
  }

  /**
   * Deletes a task from the datastore.
   *
   * @param id  the ID of the task to delete
   * @return void
   */
  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("delete from task where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}
