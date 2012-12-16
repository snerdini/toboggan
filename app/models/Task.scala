package models

/**
 * Created with IntelliJ IDEA.
 * User: steve
 * Date: 12/16/12
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */

case class Task(id: Long, label: String)

object Task {

  def all(): List[Task] = Nil

  def create(label: String) {}

  def delete(id: Long) {}
}
