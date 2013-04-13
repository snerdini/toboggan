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

  def all(pageSize: Long = 5, pageIndex: Long = 0): List[Task] = DB.withConnection { implicit c =>
    val offset = pageSize * pageIndex
    SQL("select * from task limit {pageSize} offset {offset}")
      .on('pageSize -> pageSize, 'offset -> offset)
      .as(task *)
  }

  def count = DB.withConnection { implicit c =>
    SQL("select count(*) from task")
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
