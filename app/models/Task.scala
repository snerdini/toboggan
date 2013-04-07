package models

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._
import anorm.~

/**
 * Created with IntelliJ IDEA.
 * User: steve
 * Date: 12/16/12
 * Time: 12:18 PM
 */

case class Task(id: Long, label: String)

object Task {
  // Enable manual conversion to JSON object for async client requests
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

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String) = DB.withConnection { implicit c =>
    SQL("insert into task (label) values ({label})").on(
      'label -> label
    ).executeUpdate()
  }

  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("delete from task where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}
